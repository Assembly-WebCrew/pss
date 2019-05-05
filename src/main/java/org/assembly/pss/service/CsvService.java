package org.assembly.pss.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.RollbackException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assembly.pss.bean.ImportResult;
import org.assembly.pss.bean.persistence.entity.Event;
import org.assembly.pss.bean.persistence.entity.Location;
import org.assembly.pss.database.Database;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.springframework.stereotype.Service;

@Service
public class CsvService {

    private static final Logger LOG = LogManager.getLogger(CsvService.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Resource
    private Database database;

    /**
     * A map of Column names as key, and a Pair of getters and setters as value
     */
    private final Map<String, Pair<Function<Event, String>, BiConsumer<Event, String>>> eventColumns = new LinkedHashMap<>();

    /**
     * Define columns names and mappings for exportable data
     */
    @PostConstruct
    private void initColumns() {
        eventColumns.put("name", Pair.of(Event::getName, Event::setName));
        eventColumns.put("description", Pair.of(Event::getDescription, Event::setDescription));
        eventColumns.put("startTime", Pair.of(time(Event::getStartTime), time(Event::setStartTime)));
        eventColumns.put("endTime", Pair.of(time(Event::getEndTime), time(Event::setEndTime)));
        eventColumns.put("originalStartTime", Pair.of(time(Event::getOriginalStartTime), time(Event::setOriginalStartTime)));
        eventColumns.put("endTime", Pair.of(time(Event::getEndTime), time(Event::setEndTime)));
        eventColumns.put("url", Pair.of(Event::getUrl, Event::setUrl));
        eventColumns.put("mediaUrl", Pair.of(Event::getMediaUrl, Event::setMediaUrl));
        eventColumns.put("location", Pair.of(location(Event::getLocation), location(Event::setLocation)));
        eventColumns.put("party", Pair.of(Event::getParty, Event::setParty));
        eventColumns.put("isPublic", Pair.of(bool(Event::getIsPublic), bool(Event::setIsPublic)));
        eventColumns.put("prepStartTime", Pair.of(time(Event::getPrepStartTime), time(Event::setPrepStartTime)));
        eventColumns.put("postEndTime", Pair.of(time(Event::getPostEndTime), time(Event::setPostEndTime)));
    }

    /**
     * Import events from a CSV file
     *
     * @param inputStream input stream for the CSV
     * @return Result of the import operation
     */
    public ImportResult importEvents(InputStream inputStream) {
        ImportResult result = new ImportResult();
        Map<Event, Exception> invalidEvents = new HashMap<>();
        Set<String> warnings = new HashSet<>();
        int success = 0;
        try {
            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(inputStream, "utf-8")));

            List<String> headers = new ArrayList<>();

            String[] line = reader.readNext();
            Stream.of(line).forEach(headers::add);
            while ((line = reader.readNext()) != null) {
                Event event = parseEvent(headers, warnings, line);
                try {
                    database.merge(event);
                    success++;
                } catch (Exception ex) {
                    LOG.warn("Attempted to import invalid event: " + event.toString(), ex);
                    invalidEvents.put(event, ex);
                }
            }
        } catch (IOException ex) {
            LOG.error("Can't read CSV", ex);
        }
        result.setSuccessCount(success);
        result.setFailureCount(invalidEvents.size());
        result.setErrors(invalidEvents.entrySet().stream().map(e -> prettyError(e.getKey(), e.getValue())).collect(Collectors.toList()));
        result.setWarnings(new ArrayList<>(warnings));
        return result;
    }

    public void exportEvents(Writer writer, String party) {
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(eventColumns.keySet().stream().toArray(String[]::new));
        List<Event> events = database.getEvents(party);
        if (CollectionUtils.isNotEmpty(events)) {
            events.forEach(event -> csvWriter.writeNext(eventColumns.values().stream().map(funcs -> funcs.getLeft().apply(event)).toArray(String[]::new)));
        }
    }

    private Event parseEvent(List<String> headers, Set<String> warnings, String... line) {
        Event event = new Event();
        for (int i = 0; i < line.length; i++) {
            String value = line[i];
            String currentColumn = headers.get(i);
            if (currentColumn != null) {
                Pair<Function<Event, String>, BiConsumer<Event, String>> funcs = eventColumns.get(currentColumn);
                if (funcs != null) {
                    funcs.getRight().accept(event, value);
                } else {
                    warnings.add("Ignoring unknown column: '" + currentColumn + '\'');
                }
            }
        }
        return event;
    }

    private String prettyError(Event event, Exception ex) {
        if (ex instanceof RollbackException
                && ex.getCause() instanceof DatabaseException
                && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
            return "Event: '" + event.getName() + "': " + ex.getCause().getCause().getMessage();
        }
        return "Event: '" + event.getName() + "': " + ex.getMessage();
    }

    private Function<Event, String> time(Function<Event, Long> getter) {
        return event -> {
            try {
                return new SimpleDateFormat(DATE_FORMAT).format(new Date(getter.apply(event)));
            } catch (NullPointerException ex) {
                return null;
            }
        };
    }

    private BiConsumer<Event, String> time(BiConsumer<Event, Long> setter) {
        return (event, time) -> {
            try {
                setter.accept(event, Long.valueOf(time)); // direct timestamp in ms
            } catch (NumberFormatException | NullPointerException ex) {
                try {
                    setter.accept(event, new SimpleDateFormat(DATE_FORMAT).parse(time).getTime());
                } catch (ParseException | NullPointerException ex1) {
                }
            }
        };
    }

    private Function<Event, String> bool(Function<Event, Boolean> getter) {
        return event -> Boolean.TRUE.equals(getter.apply(event)) ? "true" : "false";
    }

    private BiConsumer<Event, String> bool(BiConsumer<Event, Boolean> setter) {
        return (event, b) -> {
            if (StringUtils.isNotBlank(b)) {
                switch (b.toLowerCase().trim()) {
                    case "true":
                    case "yes":
                    case "1":
                        setter.accept(event, Boolean.TRUE);
                        break;
                    default:
                        setter.accept(event, Boolean.FALSE);
                        break;
                }
            }
        };
    }

    private Function<Event, String> location(Function<Event, Location> getter) {
        return event -> {
            Location l = getter.apply(event);
            if (l != null) {
                return l.getName();
            }
            return null;
        };
    }

    private BiConsumer<Event, String> location(BiConsumer<Event, Location> setter) {
        return (event, name) -> {
            Location location = database.getLocation(name);
            if (location == null) {
                location = new Location();
                location.setName(name);
            }
            setter.accept(event, location);
        };
    }
}

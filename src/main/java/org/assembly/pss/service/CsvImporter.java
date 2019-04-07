package org.assembly.pss.service;

import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assembly.pss.bean.persistence.entity.Event;
import org.assembly.pss.bean.persistence.entity.Location;
import org.assembly.pss.database.Database;
import org.springframework.stereotype.Service;

@Service
public class CsvImporter {

    private static final Logger LOG = LogManager.getLogger(CsvImporter.class);
    private static final Function<String, String> COLUMN_NAME_CLEANER = s -> s.toLowerCase().replace("_", "").replaceAll("\\s", "");

    @Resource
    private Database database;

    /**
     * Import events from a CSV file
     *
     * @param inputStream input stream for the CSV
     * @param defaultParty a default party if there's no separate party column
     */
    public void importEvents(InputStream inputStream, String defaultParty) {
        try {
            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(inputStream)));

            List<String> headers = new ArrayList<>();
            Map<Event, String> invalidEvents = new HashMap<>();

            String[] line = reader.readNext();
            Stream.of(line).map(COLUMN_NAME_CLEANER).forEach(headers::add);

            while ((line = reader.readNext()) != null) {
                Event event = parseEvent(headers, defaultParty, line);
                try {
                    database.merge(event);
                } catch (Exception ex) {
                    LOG.warn("Attempted to import invalid event: " + event.toString(), ex);
                    invalidEvents.put(event, ex.getMessage());
                }
            }
        } catch (IOException ex) {
            LOG.error("Can't read CSV", ex);
        } // TODO: return some sort of "import results" summary with number of successful and unsuccessful items as well as reasons for the failed ones
    }

    private Event parseEvent(List<String> headers, String defaultParty, String... line) {
        Event event = new Event();
        event.setParty(defaultParty);
        for (int i = 0; i < line.length; i++) {
            String value = line[i];
            String currentColumn = headers.get(i);
            if (currentColumn != null) {
                switch (currentColumn) {
                    case "name":
                        event.setName(value);
                        break;
                    case "description":
                        event.setDescription(value);
                        break;
                    case "starttime":
                        event.setStartTime(parseTime(value));
                        break;
                    case "originalstarttime":
                        event.setOriginalStartTime(parseTime(value));
                        break;
                    case "endtime":
                        event.setEndTime(parseTime(value));
                        break;
                    case "url":
                        event.setUrl(value);
                        break;
                    case "mediaurl":
                        event.setMediaUrl(value);
                        break;
                    case "location":
                        Location location = database.getLocation(value);
                        if (location == null) {
                            location = new Location();
                            location.setName(value);
                        }
                        event.setLocation(location);
                        break;
                    case "party":
                        event.setParty(value);
                        break;
                    case "ispublic":
                        if (StringUtils.isNotBlank(value)) {
                            switch (value.toLowerCase().trim()) {
                                case "true":
                                case "yes":
                                case "1":
                                    event.setIsPublic(Boolean.TRUE);
                                    break;
                                default:
                                    event.setIsPublic(Boolean.FALSE);
                                    break;
                            }
                        }
                        break;
                    case "prepstarttime":
                        event.setPrepStartTime(parseTime(value));
                        break;
                    case "postendtime":
                        event.setPostEndTime(parseTime(value));
                        break;
                    default:
                        LOG.warn("Unknown column: \"" + currentColumn + '"');
                        break;
                }
            }
        }
        return event;
    }

    private Long parseTime(String input) {
        try {
            return Long.valueOf(input); // direct timestamp in ms
        } catch (NumberFormatException | NullPointerException ex) {
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(input).getTime();
        } catch (ParseException | NullPointerException ex) {
            return null;
        }
    }
}

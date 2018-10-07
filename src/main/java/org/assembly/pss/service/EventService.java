package org.assembly.pss.service;

import java.util.List;
import org.assembly.pss.bean.persistence.PublicEvent;
import org.assembly.pss.database.Database;

/**
 * Possibly unnecessary overhead to have a service between the controllers and
 * the database, but I'll leave this here for now...
 */
public class EventService {

    private final Database db = new Database();

    public List<PublicEvent> getPublicEvents(String party, Long location, String tag) {
        return db.getPublicEvents(party, location, tag);
    }
}

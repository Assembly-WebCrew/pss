package org.assembly.pss.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.assembly.pss.bean.persistence.Event;
import org.assembly.pss.bean.persistence.PublicEvent;
import org.assembly.pss.config.PropertyConfig;

public class Database {

    private EntityManagerFactory factory;

    public Database() {
        Map<String, String> dbProps = new HashMap<>();
        dbProps.put("javax.persistence.jdbc.url", PropertyConfig.get("db.url") + '?' + PropertyConfig.get("db.options"));
        dbProps.put("javax.persistence.jdbc.user", PropertyConfig.get("db.user"));
        dbProps.put("javax.persistence.jdbc.password", PropertyConfig.get("db.password"));
        factory = Persistence.createEntityManagerFactory("pss", dbProps);
        try {
            EntityManager em = factory.createEntityManager();
            Query query = em.createNativeQuery("SHOW TABLES;");
            List result = query.getResultList();
            em.close();
            if (result == null || result.size() < 1) {
                throw new PersistenceException("Empty database");
            }
        } catch (PersistenceException ex) {
            throw new IllegalStateException("Dynamic database is empty or not reachable. Attempted: " + dbProps.get("javax.persistence.jdbc.url"), ex);
        }
    }

    /**
     * Query public events
     *
     * @param party required
     * @param locationId optional
     * @param tag optional
     * @return All public events matching the criteria
     */
    public List<PublicEvent> getPublicEvents(String party, Long locationId, String tag) {
        EntityManager em = factory.createEntityManager();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("party", party);
            String queryString = "select distinct e from PublicEvent e join e.tags t where e.party = :party and e.isPublic = true";
            if (locationId != null) {
                queryString += " and e.location.id = :locationId";
                params.put("locationId", locationId);
            }
            if (StringUtils.isNotBlank(tag)) {
                queryString += " and t.key = :tag";
                params.put("tag", tag);
            }
            TypedQuery<PublicEvent> q = em.createQuery(queryString, PublicEvent.class);
            params.forEach(q::setParameter);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get all events for a party including non-public ones
     *
     * @param party
     * @return All events for the party
     */
    public List<Event> getEventsByParty(String party) {
        EntityManager em = factory.createEntityManager();
        try {
            String queryString = "from Event t where t.party = :party";
            TypedQuery<Event> q = em.createQuery(queryString, Event.class);
            q.setParameter("party", party);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}

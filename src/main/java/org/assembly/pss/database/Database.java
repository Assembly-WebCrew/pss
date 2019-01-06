package org.assembly.pss.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.assembly.pss.bean.persistence.Event;
import org.assembly.pss.bean.persistence.Location;
import org.assembly.pss.bean.persistence.PublicEvent;
import org.assembly.pss.bean.persistence.Tag;
import org.assembly.pss.config.PropertyConfig;
import org.springframework.stereotype.Service;

@Service
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
            throw new IllegalStateException("Database is empty or not reachable. Attempted: " + dbProps.get("javax.persistence.jdbc.url"), ex);
        }
    }

    /**
     * Merge an object with the database; essentially a "insert or update" with
     * slight differences on how referred objects are handled
     *
     * @param <T> type of the object
     * @param obj The object to merge
     * @return The resulting object after the merge
     */
    public <T extends Object> T merge(T obj) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            T o = em.merge(obj);
            em.getTransaction().commit();
            return o;
        } finally {
            em.close();
        }
    }

    /**
     * Remove and object from the database
     *
     * @param obj The object to remove
     */
    public void remove(Object obj) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(obj));
            em.getTransaction().commit();
        } finally {
            em.close();
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
    public List<Event> getEvents(String party) {
        EntityManager em = factory.createEntityManager();
        try {
            String queryString = "from Event e where e.party = :party";
            TypedQuery<Event> q = em.createQuery(queryString, Event.class);
            q.setParameter("party", party);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get a single event by its ID
     *
     * @param id
     * @return The Event
     */
    public Event getEvent(Integer id) {
        EntityManager em = factory.createEntityManager();
        try {
            String queryString = "from Event e where e.id = :id";
            TypedQuery<Event> q = em.createQuery(queryString, Event.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Get all tags
     *
     * @return All tags in the database, even the unused ones
     */
    public List<Tag> getTags() {
        EntityManager em = factory.createEntityManager();
        try {
            String queryString = "from Tag t";
            TypedQuery<Tag> q = em.createQuery(queryString, Tag.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get a single tag by its ID
     *
     * @param id
     * @return The Event
     */
    public Tag getTag(Integer id) {
        EntityManager em = factory.createEntityManager();
        try {
            String queryString = "from Tag t where t.id = :id";
            TypedQuery<Tag> q = em.createQuery(queryString, Tag.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Get all locations
     *
     * @return All locations in the database, even the unused ones
     */
    public List<Location> getLocations() {
        EntityManager em = factory.createEntityManager();
        try {
            String queryString = "from Location l";
            TypedQuery<Location> q = em.createQuery(queryString, Location.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get a single location by its ID
     *
     * @param id
     * @return The Event
     */
    public Location getLocation(Integer id) {
        EntityManager em = factory.createEntityManager();
        try {
            String queryString = "from Location l where l.id = :id";
            TypedQuery<Location> q = em.createQuery(queryString, Location.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Get all parties
     *
     * @return All parties referred to by at least one event
     */
    public List<String> getParties() {
        EntityManager em = factory.createEntityManager();
        try {
            Query q = em.createQuery("SELECT DISTINCT e.party FROM Event e");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}

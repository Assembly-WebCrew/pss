package org.assembly.pss.bean.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @Column(updatable = false, nullable = false)
    private String key;
    private String displayName;
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<PublicEvent> publicEvents;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<PublicEvent> getPublicEvents() {
        return publicEvents;
    }

    public void setPublicEvents(List<PublicEvent> publicEvents) {
        this.publicEvents = publicEvents;
    }
}

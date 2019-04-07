package org.assembly.pss.bean.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.assembly.pss.bean.persistence.AbstractNamedEntity;

@Entity
@Table(name = "locations")
public class Location extends AbstractNamedEntity implements Serializable {

    private String description;
    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

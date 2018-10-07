package org.assembly.pss.bean.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public class PublicEvent extends AbstractEvent {

    @JsonIgnore // a hack to hide public status from public events while still keeping it on AbstractEvent for JPA
    private Boolean isPublic;
}

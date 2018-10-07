package org.assembly.pss.bean.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public class Event extends AbstractEvent {

    private Long prepStartTime;
    private Long postEndTime;

    public Long getPrepStartTime() {
        return prepStartTime;
    }

    public void setPrepStartTime(Long prepStartTime) {
        this.prepStartTime = prepStartTime;
    }

    public Long getPostEndTime() {
        return postEndTime;
    }

    public void setPostEndTime(Long postEndTime) {
        this.postEndTime = postEndTime;
    }
}

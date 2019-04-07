package org.assembly.pss.bean.persistence;

import org.assembly.pss.bean.persistence.entity.Tag;
import org.assembly.pss.bean.persistence.entity.Location;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEvent extends AbstractNamedEntity implements Serializable {

    private String description;
    @Column(nullable = false)
    private Long startTime;
    private Long originalStartTime;
    @Column(nullable = false)
    private Long endTime;
    private String url;
    private String mediaUrl;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Location location;
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "events_tags",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;
    @Column(nullable = false)
    private String party;
    @Column(nullable = false)
    private Boolean isPublic;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getOriginalStartTime() {
        return originalStartTime;
    }

    public void setOriginalStartTime(Long originalStartTime) {
        this.originalStartTime = originalStartTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}

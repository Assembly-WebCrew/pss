package org.assembly.pss.bean.persistence;

import javax.persistence.MappedSuperclass;

/**
 * A named entity is an entity with an unique name
 */
@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

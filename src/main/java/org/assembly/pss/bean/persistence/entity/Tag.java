package org.assembly.pss.bean.persistence.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.assembly.pss.bean.persistence.AbstractNamedEntity;

@Entity
@Table(name = "tags")
public class Tag extends AbstractNamedEntity implements Serializable {
}

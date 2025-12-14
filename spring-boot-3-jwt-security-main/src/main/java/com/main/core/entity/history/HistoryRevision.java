package com.main.core.entity.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import java.io.Serializable;

@Entity
@RevisionEntity(HistoryListener.class)
@Table(name = "HISTORY_REVISIONS")
public class HistoryRevision
        extends DefaultTrackingModifiedEntitiesRevisionEntity
        implements Serializable {

    @Column(name = "CREATOR")
    private Long creator;

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }
}

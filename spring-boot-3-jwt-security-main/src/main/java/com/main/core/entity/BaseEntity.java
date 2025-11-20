package com.main.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.core.Setup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.logging.Level;

import org.hibernate.envers.Audited;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Version
    @ColumnDefault("0")
    @Column
    private long version;

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;

    @CreatedBy
    @Column(
            nullable = true,
            updatable = false
    )
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;


    @JsonIgnore
    public String getLabel() {
        Field labelField = Setup.getLabelField(this.getClass());
        if (labelField != null) {
            labelField.setAccessible(true);
            try {
                return (String) labelField.get(this);
            } catch (IllegalArgumentException
                     | IllegalAccessException ex) {
                    //
            }
        }
        return getId() + "";
    }
}


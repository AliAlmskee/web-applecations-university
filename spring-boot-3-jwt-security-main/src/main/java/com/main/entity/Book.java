package com.main.entity;

import com.main.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Book extends BaseEntity {

    private String author;
    private String isbn;
}

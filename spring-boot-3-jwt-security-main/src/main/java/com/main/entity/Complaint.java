package com.main.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.main.core.entity.BaseEntity;
import com.main.core.serializer.IdLabelSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Audited
public class Complaint extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @ElementCollection
    @CollectionTable(name = "complaint_files", joinColumns = @JoinColumn(name = "complaint_id"))
    @Column(name = "file_path")
    private List<String> files = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintType type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String complainedAbout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complainant_user_id", nullable = false)
    @JsonSerialize(using = IdLabelSerializer.class)
    private User complainant;
}


package com.main.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.main.core.entity.BaseEntity;
import com.main.core.serializer.IdLabelSerializer;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

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

    public Complaint() {
    }

    private Complaint(Builder builder) {
        this.status = builder.status;
        this.files = builder.files != null ? builder.files : new ArrayList<>();
        this.type = builder.type;
        this.location = builder.location;
        this.description = builder.description;
        this.complainedAbout = builder.complainedAbout;
        this.complainant = builder.complainant;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ComplaintStatus status;
        private List<String> files;
        private ComplaintType type;
        private String location;
        private String description;
        private String complainedAbout;
        private User complainant;

        public Builder status(ComplaintStatus status) {
            this.status = status;
            return this;
        }

        public Builder files(List<String> files) {
            this.files = files;
            return this;
        }

        public Builder type(ComplaintType type) {
            this.type = type;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder complainedAbout(String complainedAbout) {
            this.complainedAbout = complainedAbout;
            return this;
        }

        public Builder complainant(User complainant) {
            this.complainant = complainant;
            return this;
        }

        public Complaint build() {
            return new Complaint(this);
        }
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public ComplaintType getType() {
        return type;
    }

    public void setType(ComplaintType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComplainedAbout() {
        return complainedAbout;
    }

    public void setComplainedAbout(String complainedAbout) {
        this.complainedAbout = complainedAbout;
    }

    public User getComplainant() {
        return complainant;
    }

    public void setComplainant(User complainant) {
        this.complainant = complainant;
    }
}


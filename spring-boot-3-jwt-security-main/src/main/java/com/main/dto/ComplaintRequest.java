package com.main.dto;

import com.main.entity.ComplaintStatus;
import com.main.entity.ComplaintType;

import java.util.List;

public class ComplaintRequest {
    private ComplaintStatus status;
    private List<String> files;
    private ComplaintType type;
    private String location;
    private String description;
    private String complainedAbout;

    public ComplaintRequest() {
    }

    private ComplaintRequest(Builder builder) {
        this.status = builder.status;
        this.files = builder.files;
        this.type = builder.type;
        this.location = builder.location;
        this.description = builder.description;
        this.complainedAbout = builder.complainedAbout;
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

        public ComplaintRequest build() {
            return new ComplaintRequest(this);
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
}


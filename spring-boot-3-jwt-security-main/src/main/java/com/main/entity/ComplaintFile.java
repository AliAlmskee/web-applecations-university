package com.main.entity;

import com.main.core.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "complaint_files")
@Audited
public class ComplaintFile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @Column
    private String contentType;

    @Column
    private Long fileSize;

    public ComplaintFile() {
    }

    public ComplaintFile(Complaint complaint, String filePath, String fileName, String contentType, Long fileSize) {
        this.complaint = complaint;
        this.filePath = filePath;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}


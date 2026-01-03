package com.main.repository;

import com.main.entity.ComplaintFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintFileRepository extends JpaRepository<ComplaintFile, Long> {
    List<ComplaintFile> findByComplaintId(Long complaintId);
}


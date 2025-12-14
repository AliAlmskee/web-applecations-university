package com.main.repository;

import com.main.core.repository.BaseRepository;
import com.main.entity.Complaint;
import com.main.entity.ComplaintStatus;
import com.main.entity.ComplaintType;
import com.main.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComplaintRepository extends BaseRepository<Complaint> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Complaint c WHERE c.id = :id")
    Optional<Complaint> findByIdWithLock(@Param("id") Long id);
    
    List<Complaint> findByComplainantId(Long complainantId);
    
    List<Complaint> findByComplainedAbout(String complainedAbout);
    
    List<Complaint> findByStatus(ComplaintStatus status);
    
    List<Complaint> findByType(ComplaintType type);
}


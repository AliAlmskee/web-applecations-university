package com.main.services;

import com.main.entity.Complaint;
import com.main.entity.ComplaintStatus;
import com.main.entity.ComplaintType;
import com.main.entity.User;
import com.main.repository.ComplaintRepository;
import com.main.repository.UserRepository;
import com.main.dto.ComplaintRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final AuditorAware<Integer> auditorAware;

    @CacheEvict(value = "complaints", allEntries = true)
    @Transactional
    public Complaint save(ComplaintRequest request) {
        Integer currentAuditorId = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("No authenticated user found"));
        
        User complainant = userRepository.findById(currentAuditorId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + currentAuditorId));

        Complaint complaint = Complaint.builder()
                .status(request.getStatus() != null ? request.getStatus() : ComplaintStatus.PENDING)
                .files(request.getFiles() != null ? request.getFiles() : List.of())
                .type(request.getType())
                .location(request.getLocation())
                .description(request.getDescription())
                .complainedAbout(request.getComplainedAbout())
                .complainant(complainant)
                .build();

        return complaintRepository.save(complaint);
    }

    @Cacheable("complaints")
    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    @Transactional
    public Complaint findById(int id) {
        return complaintRepository.findByIdWithLock(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with ID: " + id));
    }

    @CacheEvict(value = "complaints", allEntries = true)
    @Transactional
    public Complaint update(int id, ComplaintRequest request) {
        Complaint existing = findById(id);

        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        if (request.getFiles() != null) {
            existing.setFiles(request.getFiles());
        }
        if (request.getType() != null) {
            existing.setType(request.getType());
        }
        if (request.getLocation() != null) {
            existing.setLocation(request.getLocation());
        }
        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }
        if (request.getComplainedAbout() != null) {
            existing.setComplainedAbout(request.getComplainedAbout());
        }

        return complaintRepository.save(existing);
    }

    @CacheEvict(value = "complaints", allEntries = true)
    @Transactional
    public void delete(int id) {
        Complaint existing = findById(id);
        complaintRepository.delete(existing);
    }

    public List<Complaint> findByComplainantId(Integer complainantId) {
        return complaintRepository.findByComplainantId(complainantId);
    }

    public List<Complaint> findByComplainedAbout(String complainedAbout) {
        return complaintRepository.findByComplainedAbout(complainedAbout);
    }

    public List<Complaint> findByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status);
    }

    public List<Complaint> findByType(ComplaintType type) {
        return complaintRepository.findByType(type);
    }
}


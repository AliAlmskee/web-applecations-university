package com.main.services;

import com.main.entity.Complaint;
import com.main.entity.ComplaintFile;
import com.main.entity.ComplaintStatus;
import com.main.entity.ComplaintType;
import com.main.entity.User;
import com.main.repository.ComplaintFileRepository;
import com.main.repository.ComplaintRepository;
import com.main.repository.UserRepository;
import com.main.dto.ComplaintRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintFileRepository complaintFileRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final AuditorAware<Long> auditorAware;

    @CacheEvict(value = "complaints", allEntries = true)
    @Transactional
    public Complaint save(ComplaintRequest request) {
        Long currentAuditorId = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("No authenticated user found"));
        
        User complainant = userRepository.findById(currentAuditorId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + currentAuditorId));

        Complaint complaint = Complaint.builder()
                .status(ComplaintStatus.PENDING)
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
    public Complaint findById(Long id) {
        return complaintRepository.findByIdWithLock(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with ID: " + id));
    }

    @CacheEvict(value = "complaints", allEntries = true)
    @Transactional
    public Complaint update(Long id, ComplaintRequest request) {
        Complaint existing = findById(id);

        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
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
    public void delete(Long id) {
        Complaint existing = findById(id);
        complaintRepository.delete(existing);
    }

    public List<Complaint> findByComplainantId(Long complainantId) {
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

    @CacheEvict(value = "complaints", allEntries = true)
    @Transactional
    public ComplaintFile uploadFile(int complaintId, MultipartFile file) throws Exception {
        Complaint complaint = findById(complaintId);
        
        // Create path for complaint files: complaints/{complaintId}/filename
        Path filePath = Paths.get("complaints", String.valueOf(complaintId));
        
        // Save file using FileService
        String savedPath = fileService.createFile(file, filePath);
        
        // Create ComplaintFile entity
        ComplaintFile complaintFile = new ComplaintFile(
                complaint,
                savedPath,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize()
        );
        
        // Save to database
        return complaintFileRepository.save(complaintFile);
    }

    @CacheEvict(value = "complaints", allEntries = true)
    @Transactional
    public void deleteFile(Long fileId) {
        ComplaintFile file = complaintFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));
        complaintFileRepository.delete(file);
    }

    public List<ComplaintFile> getComplaintFiles(int complaintId) {
        return complaintFileRepository.findByComplaintId(complaintId);
    }
}


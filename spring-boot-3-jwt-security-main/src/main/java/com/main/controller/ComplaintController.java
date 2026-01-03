package com.main.controller;

import com.main.entity.Complaint;
import com.main.entity.ComplaintFile;
import com.main.entity.ComplaintStatus;
import com.main.entity.ComplaintType;
import com.main.dto.ComplaintRequest;
import com.main.services.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/complaints")
public class ComplaintController {

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ComplaintRequest request) {
        Complaint complaint = service.save(request);
        return ResponseEntity.accepted().body(complaint);
    }

    @GetMapping
    public ResponseEntity<List<Complaint>> findAllComplaints() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> findComplaintById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComplaint(
            @PathVariable int id,
            @RequestBody ComplaintRequest request
    ) {
        Complaint complaint = service.update(id, request);
        return ResponseEntity.ok(complaint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComplaint(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.ok("Complaint deleted successfully");
    }

    @GetMapping("/complainant/{complainantId}")
    public ResponseEntity<List<Complaint>> findByComplainantId(@PathVariable Integer complainantId) {
        return ResponseEntity.ok(service.findByComplainantId(complainantId));
    }

    @GetMapping("/complained-about")
    public ResponseEntity<List<Complaint>> findByComplainedAbout(@RequestParam String complainedAbout) {
        return ResponseEntity.ok(service.findByComplainedAbout(complainedAbout));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Complaint>> findByStatus(@PathVariable ComplaintStatus status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Complaint>> findByType(@PathVariable ComplaintType type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @PostMapping("/{id}/files")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            ComplaintFile complaintFile = service.uploadFile(id, file);
            return ResponseEntity.ok(complaintFile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<ComplaintFile>> getComplaintFiles(@PathVariable int id) {
        return ResponseEntity.ok(service.getComplaintFiles(id));
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        service.deleteFile(fileId);
        return ResponseEntity.ok("File deleted successfully");
    }
}


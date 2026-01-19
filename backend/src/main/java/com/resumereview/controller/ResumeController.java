package com.resumereview.controller;

import com.resumereview.model.Resume;
import com.resumereview.dto.ResumeUploadResponse;
import com.resumereview.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/resumes")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * Upload a resume
     */
    @PostMapping("/upload")
    public ResponseEntity<ResumeUploadResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Received resume upload request for file: {}", file.getOriginalFilename());
            
            Resume resume = resumeService.uploadResume(file);
            
            ResumeUploadResponse response = new ResumeUploadResponse(
                    resume.getId(),
                    resume.getFileName(),
                    resume.getFileSize(),
                    "Resume uploaded successfully",
                    true
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            log.error("Error uploading resume: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResumeUploadResponse(null, null, null, "Error uploading file", false));
        }
    }

    /**
     * Get resume by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        log.info("Fetching resume with ID: {}", id);
        Resume resume = resumeService.getResumeById(id);
        return ResponseEntity.ok(resume);
    }

    /**
     * Get all resumes
     */
    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        log.info("Fetching all resumes");
        List<Resume> resumes = resumeService.getAllResumes();
        return ResponseEntity.ok(resumes);
    }

    /**
     * Get resume status
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getResumeStatus(@PathVariable Long id) {
        log.info("Fetching status for resume ID: {}", id);
        String status = resumeService.getResumeStatus(id);
        return ResponseEntity.ok(status);
    }

    /**
     * Update resume status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Resume> updateResumeStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("Updating status for resume ID: {} to {}", id, status);
        Resume resume = resumeService.updateResumeStatus(id, status);
        return ResponseEntity.ok(resume);
    }

    /**
     * Delete resume
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        log.info("Deleting resume with ID: {}", id);
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }
}

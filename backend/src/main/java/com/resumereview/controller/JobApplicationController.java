package com.resumereview.controller;

import com.resumereview.model.JobApplication;
import com.resumereview.dto.JobApplicationDTO;
import com.resumereview.service.JobApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-applications")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    /**
     * Apply for a job
     */
    @PostMapping("/apply")
    public ResponseEntity<JobApplicationDTO> applyForJob(
            @RequestParam Long jobSuggestionId,
            @RequestParam Long resumeId,
            @RequestParam(required = false) String applicationNotes) {
        
        log.info("Applying for job suggestion ID: {} with resume ID: {}", jobSuggestionId, resumeId);
        
        JobApplication application = jobApplicationService.applyForJob(jobSuggestionId, resumeId, applicationNotes);
        JobApplicationDTO dto = jobApplicationService.convertToDTO(application);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Get applications for a resume
     */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<JobApplicationDTO>> getApplicationsForResume(@PathVariable Long resumeId) {
        log.info("Fetching job applications for resume ID: {}", resumeId);
        
        List<JobApplication> applications = jobApplicationService.getApplicationsForResume(resumeId);
        List<JobApplicationDTO> dtos = jobApplicationService.convertListToDTO(applications);
        
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get single application
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationDTO> getApplication(@PathVariable Long id) {
        log.info("Fetching job application with ID: {}", id);
        
        JobApplication application = jobApplicationService.getApplicationById(id);
        JobApplicationDTO dto = jobApplicationService.convertToDTO(application);
        
        return ResponseEntity.ok(dto);
    }

    /**
     * Update application status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<JobApplicationDTO> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        
        log.info("Updating application ID: {} status to: {}", id, status);
        
        JobApplication application = jobApplicationService.updateApplicationStatus(id, status);
        JobApplicationDTO dto = jobApplicationService.convertToDTO(application);
        
        return ResponseEntity.ok(dto);
    }

    /**
     * Update application with response
     */
    @PutMapping("/{id}/response")
    public ResponseEntity<JobApplicationDTO> updateApplicationResponse(
            @PathVariable Long id,
            @RequestParam String responseStatus,
            @RequestParam String responseMessage) {
        
        log.info("Updating application ID: {} response", id);
        
        JobApplication application = jobApplicationService.updateApplicationResponse(id, responseStatus, responseMessage);
        JobApplicationDTO dto = jobApplicationService.convertToDTO(application);
        
        return ResponseEntity.ok(dto);
    }

    /**
     * Delete application
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        log.info("Deleting job application with ID: {}", id);
        
        jobApplicationService.deleteApplication(id);
        
        return ResponseEntity.noContent().build();
    }
}

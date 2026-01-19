package com.resumereview.service;

import com.resumereview.model.JobApplication;
import com.resumereview.model.JobSuggestion;
import com.resumereview.model.Resume;
import com.resumereview.dto.JobApplicationDTO;
import com.resumereview.repository.JobApplicationRepository;
import com.resumereview.repository.JobSuggestionRepository;
import com.resumereview.repository.ResumeRepository;
import com.resumereview.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobSuggestionRepository jobSuggestionRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    /**
     * Apply for a job
     */
    public JobApplication applyForJob(Long jobSuggestionId, Long resumeId, String applicationNotes) {
        log.info("Applying for job suggestion ID: {} with resume ID: {}", jobSuggestionId, resumeId);

        JobSuggestion jobSuggestion = jobSuggestionRepository.findById(jobSuggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Job suggestion not found with ID: " + jobSuggestionId));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        // Check if already applied
        if (jobApplicationRepository.findByJobSuggestionAndResume(jobSuggestion, resume).isPresent()) {
            throw new IllegalArgumentException("Already applied for this job");
        }

        JobApplication application = new JobApplication();
        application.setJobSuggestion(jobSuggestion);
        application.setResume(resume);
        application.setStatus("APPLIED");
        application.setApplicationNotes(applicationNotes);
        application.setAppliedAt(LocalDateTime.now());

        JobApplication savedApplication = jobApplicationRepository.save(application);
        log.info("Job application created with ID: {}", savedApplication.getId());

        return savedApplication;
    }

    /**
     * Get applications for a resume
     */
    public List<JobApplication> getApplicationsForResume(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        return jobApplicationRepository.findByResumeOrderByAppliedAtDesc(resume);
    }

    /**
     * Get single application
     */
    public JobApplication getApplicationById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with ID: " + id));
    }

    /**
     * Update application status
     */
    public JobApplication updateApplicationStatus(Long id, String status) {
        JobApplication application = getApplicationById(id);
        application.setStatus(status);
        return jobApplicationRepository.save(application);
    }

    /**
     * Update application with response
     */
    public JobApplication updateApplicationResponse(Long id, String responseStatus, String responseMessage) {
        JobApplication application = getApplicationById(id);
        application.setResponseStatus(responseStatus);
        application.setResponseMessage(responseMessage);
        application.setResponseDate(LocalDateTime.now());
        return jobApplicationRepository.save(application);
    }

    /**
     * Delete application
     */
    public void deleteApplication(Long id) {
        JobApplication application = getApplicationById(id);
        jobApplicationRepository.deleteById(id);
        log.info("Job application deleted with ID: {}", id);
    }

    /**
     * Convert to DTO
     */
    public JobApplicationDTO convertToDTO(JobApplication application) {
        JobSuggestion jobSuggestion = application.getJobSuggestion();
        
        return new JobApplicationDTO(
                application.getId(),
                jobSuggestion.getId(),
                jobSuggestion.getJobTitle(),
                jobSuggestion.getCompany(),
                application.getStatus(),
                application.getApplicationNotes(),
                application.getResponseStatus()
        );
    }

    /**
     * Convert list to DTOs
     */
    public List<JobApplicationDTO> convertListToDTO(List<JobApplication> applications) {
        return applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

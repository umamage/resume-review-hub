package com.resumereview.controller;

import com.resumereview.model.JobSuggestion;
import com.resumereview.dto.JobSuggestionDTO;
import com.resumereview.service.JobSuggestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-suggestions")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class JobSuggestionController {

    @Autowired
    private JobSuggestionService jobSuggestionService;

    /**
     * Generate job suggestions for a resume
     */
    @PostMapping("/generate/{resumeId}")
    public ResponseEntity<List<JobSuggestionDTO>> generateJobSuggestions(@PathVariable Long resumeId) {
        log.info("Generating job suggestions for resume ID: {}", resumeId);
        
        List<JobSuggestion> suggestions = jobSuggestionService.generateJobSuggestions(resumeId);
        List<JobSuggestionDTO> dtos = jobSuggestionService.convertListToDTO(suggestions);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    /**
     * Get job suggestions for a resume
     */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<List<JobSuggestionDTO>> getJobSuggestions(@PathVariable Long resumeId) {
        log.info("Fetching job suggestions for resume ID: {}", resumeId);
        
        List<JobSuggestion> suggestions = jobSuggestionService.getJobSuggestions(resumeId);
        List<JobSuggestionDTO> dtos = jobSuggestionService.convertListToDTO(suggestions);
        
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get single job suggestion
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobSuggestionDTO> getJobSuggestion(@PathVariable Long id) {
        log.info("Fetching job suggestion with ID: {}", id);
        
        JobSuggestion suggestion = jobSuggestionService.getJobSuggestionById(id);
        JobSuggestionDTO dto = jobSuggestionService.convertToDTO(suggestion);
        
        return ResponseEntity.ok(dto);
    }
}

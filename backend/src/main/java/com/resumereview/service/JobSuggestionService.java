package com.resumereview.service;

import com.resumereview.model.JobSuggestion;
import com.resumereview.model.Resume;
import com.resumereview.dto.JobSuggestionDTO;
import com.resumereview.repository.JobSuggestionRepository;
import com.resumereview.repository.ResumeRepository;
import com.resumereview.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobSuggestionService {

    @Autowired
    private JobSuggestionRepository jobSuggestionRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    /**
     * Generate job suggestions based on resume
     */
    public List<JobSuggestion> generateJobSuggestions(Long resumeId) {
        log.info("Generating job suggestions for resume ID: {}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        List<JobSuggestion> suggestions = new ArrayList<>();
        String resumeText = resume.getExtractedText().toLowerCase();

        // Sample job suggestions based on keywords
        suggestions.add(createJobSuggestion(resume, "Senior Software Engineer", "Tech Corp", 
                calculateMatchScore(resumeText, "java", "spring", "microservices")));
        
        suggestions.add(createJobSuggestion(resume, "Full Stack Developer", "Digital Solutions Inc",
                calculateMatchScore(resumeText, "javascript", "react", "api")));
        
        suggestions.add(createJobSuggestion(resume, "Backend Developer", "Cloud Systems Ltd",
                calculateMatchScore(resumeText, "sql", "database", "cloud")));
        
        suggestions.add(createJobSuggestion(resume, "DevOps Engineer", "Innovation Labs",
                calculateMatchScore(resumeText, "docker", "kubernetes", "aws")));
        
        suggestions.add(createJobSuggestion(resume, "Data Engineer", "Analytics Pro",
                calculateMatchScore(resumeText, "sql", "data", "python")));

        // Save all suggestions
        suggestions = jobSuggestionRepository.saveAll(suggestions);
        log.info("Generated {} job suggestions for resume ID: {}", suggestions.size(), resumeId);

        return suggestions;
    }

    /**
     * Create a job suggestion
     */
    private JobSuggestion createJobSuggestion(Resume resume, String jobTitle, String company, Double matchScore) {
        JobSuggestion suggestion = new JobSuggestion();
        suggestion.setResume(resume);
        suggestion.setJobTitle(jobTitle);
        suggestion.setCompany(company);
        suggestion.setMatchScore(matchScore);
        suggestion.setLocation("Remote / Hybrid");
        suggestion.setEmploymentType("Full-time");
        suggestion.setStatus("ACTIVE");
        suggestion.setSuggestedAt(LocalDateTime.now());

        // Add sample data
        suggestion.setDescription(generateJobDescription(jobTitle));
        suggestion.setRequiredSkills(generateRequiredSkills(jobTitle));
        suggestion.setJobUrl("https://example.com/jobs/" + jobTitle.toLowerCase().replace(" ", "-"));

        return suggestion;
    }

    /**
     * Calculate match score based on keywords in resume
     */
    private Double calculateMatchScore(String resumeText, String... keywords) {
        int matches = 0;
        for (String keyword : keywords) {
            if (resumeText.contains(keyword)) {
                matches++;
            }
        }
        return Math.min(50.0 + (matches * 15.0), 100.0);
    }

    /**
     * Generate job description
     */
    private String generateJobDescription(String jobTitle) {
        return "We are looking for a talented " + jobTitle + " to join our team. " +
               "You will work on challenging projects using modern technologies and collaborate with a team of experienced professionals.";
    }

    /**
     * Generate required skills
     */
    private String generateRequiredSkills(String jobTitle) {
        if (jobTitle.contains("Senior")) {
            return "5+ years experience, Leadership, System Design, Problem Solving";
        } else if (jobTitle.contains("Backend")) {
            return "REST API, SQL, Java/Python, Microservices, Cloud";
        } else if (jobTitle.contains("Full Stack")) {
            return "JavaScript, React, Node.js, SQL, Git";
        } else if (jobTitle.contains("DevOps")) {
            return "Docker, Kubernetes, AWS, CI/CD, Linux";
        }
        return "Technical Skills, Problem Solving, Teamwork";
    }

    /**
     * Get job suggestions for a resume
     */
    public List<JobSuggestion> getJobSuggestions(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        return jobSuggestionRepository.findByResumeOrderByMatchScoreDesc(resume);
    }

    /**
     * Get single job suggestion
     */
    public JobSuggestion getJobSuggestionById(Long id) {
        return jobSuggestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job suggestion not found with ID: " + id));
    }

    /**
     * Convert to DTO
     */
    public JobSuggestionDTO convertToDTO(JobSuggestion suggestion) {
        return new JobSuggestionDTO(
                suggestion.getId(),
                suggestion.getJobTitle(),
                suggestion.getCompany(),
                suggestion.getDescription(),
                suggestion.getMatchScore(),
                suggestion.getLocation(),
                suggestion.getEmploymentType(),
                suggestion.getRequiredSkills(),
                suggestion.getJobUrl(),
                suggestion.getStatus()
        );
    }

    /**
     * Convert list to DTOs
     */
    public List<JobSuggestionDTO> convertListToDTO(List<JobSuggestion> suggestions) {
        return suggestions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

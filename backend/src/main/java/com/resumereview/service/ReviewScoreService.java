package com.resumereview.service;

import com.resumereview.model.Resume;
import com.resumereview.model.ReviewScore;
import com.resumereview.dto.ReviewScoreDTO;
import com.resumereview.repository.ReviewScoreRepository;
import com.resumereview.repository.ResumeRepository;
import com.resumereview.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ReviewScoreService {

    @Autowired
    private ReviewScoreRepository reviewScoreRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    /**
     * Generate review score for a resume
     */
    public ReviewScore generateReviewScore(Long resumeId) {
        log.info("Generating review score for resume ID: {}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        // Calculate scores
        double formatScore = calculateFormatScore(resume.getFileName());
        double contentScore = calculateContentScore(resume.getExtractedText());
        double keywordScore = calculateKeywordScore(resume.getExtractedText());
        double overallScore = (formatScore + contentScore + keywordScore) / 3;

        // Generate feedback and suggestions
        String feedback = generateFeedback(formatScore, contentScore, keywordScore);
        String suggestions = generateSuggestions(resume.getExtractedText());

        // Create review score
        ReviewScore reviewScore = new ReviewScore();
        reviewScore.setResume(resume);
        reviewScore.setOverallScore(Math.min(overallScore, 100.0));
        reviewScore.setFormatScore(Math.min(formatScore, 100.0));
        reviewScore.setContentScore(Math.min(contentScore, 100.0));
        reviewScore.setKeywordScore(Math.min(keywordScore, 100.0));
        reviewScore.setFeedback(feedback);
        reviewScore.setSuggestions(suggestions);
        reviewScore.setCreatedAt(LocalDateTime.now());
        reviewScore.setUpdatedAt(LocalDateTime.now());

        ReviewScore savedScore = reviewScoreRepository.save(reviewScore);
        log.info("Review score generated with ID: {}", savedScore.getId());

        return savedScore;
    }

    /**
     * Calculate format score based on filename and file properties
     */
    private double calculateFormatScore(String fileName) {
        double score = 60.0;

        // Add points for proper file extension
        if (fileName.toLowerCase().endsWith(".pdf")) {
            score += 20;
        }

        // Deduct points if filename is too long or short
        if (fileName.length() < 5 || fileName.length() > 50) {
            score -= 10;
        }

        return Math.min(score, 100.0);
    }

    /**
     * Calculate content score based on extracted text analysis
     */
    private double calculateContentScore(String text) {
        if (text == null || text.isEmpty()) {
            return 20.0;
        }

        double score = 50.0;

        // Check for common resume sections
        text = text.toLowerCase();
        if (text.contains("experience") || text.contains("employment")) score += 15;
        if (text.contains("education") || text.contains("degree")) score += 10;
        if (text.contains("skill")) score += 10;
        if (text.contains("project") || text.contains("achievement")) score += 10;
        if (text.contains("certification") || text.contains("license")) score += 5;

        // Check for contact information
        if (text.contains("email") || text.contains("@")) score += 5;
        if (text.contains("phone") || text.matches(".*\\d{3}[-.]?\\d{3}[-.]?\\d{4}.*")) score += 5;

        return Math.min(score, 100.0);
    }

    /**
     * Calculate keyword score based on industry keywords
     */
    private double calculateKeywordScore(String text) {
        if (text == null || text.isEmpty()) {
            return 20.0;
        }

        double score = 40.0;
        text = text.toLowerCase();

        // Technical skills keywords
        String[] techKeywords = {"java", "python", "javascript", "sql", "rest api", "cloud", 
                               "aws", "docker", "kubernetes", "git", "spring", "react", "angular"};
        for (String keyword : techKeywords) {
            if (text.contains(keyword)) score += 2;
        }

        // Soft skills keywords
        String[] softKeywords = {"leadership", "communication", "teamwork", "problem solving", 
                               "project management", "agile", "analytical"};
        for (String keyword : softKeywords) {
            if (text.contains(keyword)) score += 1.5;
        }

        return Math.min(score, 100.0);
    }

    /**
     * Generate feedback based on scores
     */
    private String generateFeedback(double formatScore, double contentScore, double keywordScore) {
        StringBuilder feedback = new StringBuilder();

        if (formatScore >= 80) {
            feedback.append("• Excellent resume format and structure.\n");
        } else if (formatScore >= 60) {
            feedback.append("• Good resume format with room for improvement.\n");
        } else {
            feedback.append("• Resume format needs improvement. Consider using a cleaner layout.\n");
        }

        if (contentScore >= 80) {
            feedback.append("• Strong content with comprehensive information.\n");
        } else if (contentScore >= 60) {
            feedback.append("• Decent content coverage. Add more details to key sections.\n");
        } else {
            feedback.append("• Content needs expansion. Include all important sections.\n");
        }

        if (keywordScore >= 80) {
            feedback.append("• Excellent use of industry keywords and technical terms.\n");
        } else if (keywordScore >= 60) {
            feedback.append("• Good keyword usage. Consider adding more industry-specific terms.\n");
        } else {
            feedback.append("• Add more relevant keywords to improve ATS compatibility.\n");
        }

        return feedback.toString();
    }

    /**
     * Generate suggestions for improvement
     */
    private String generateSuggestions(String text) {
        StringBuilder suggestions = new StringBuilder();

        if (text == null || text.isEmpty()) {
            return "Resume content could not be extracted. Ensure the PDF is valid.";
        }

        text = text.toLowerCase();

        if (!text.contains("experience")) {
            suggestions.append("✓ Add a detailed 'Experience' section with your work history.\n");
        }

        if (!text.contains("education")) {
            suggestions.append("✓ Include an 'Education' section with degrees and certifications.\n");
        }

        if (!text.contains("skill")) {
            suggestions.append("✓ Create a 'Skills' section highlighting technical and soft skills.\n");
        }

        if (!text.contains("project")) {
            suggestions.append("✓ Consider adding a 'Projects' section showcasing your work.\n");
        }

        if (!text.matches(".*[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}.*")) {
            suggestions.append("✓ Make sure your email address is clearly visible.\n");
        }

        if (text.length() < 500) {
            suggestions.append("✓ Expand your resume content for more detailed information.\n");
        }

        suggestions.append("✓ Ensure proper spelling and grammar throughout.\n");
        suggestions.append("✓ Use action verbs to describe your achievements.\n");
        suggestions.append("✓ Quantify your accomplishments with metrics and numbers.\n");

        return suggestions.toString();
    }

    /**
     * Get review score for a resume
     */
    public ReviewScore getReviewScore(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        return reviewScoreRepository.findByResume(resume)
                .orElseThrow(() -> new ResourceNotFoundException("Review score not found for resume ID: " + resumeId));
    }

    /**
     * Convert to DTO
     */
    public ReviewScoreDTO convertToDTO(ReviewScore reviewScore) {
        return new ReviewScoreDTO(
                reviewScore.getId(),
                reviewScore.getOverallScore(),
                reviewScore.getFormatScore(),
                reviewScore.getContentScore(),
                reviewScore.getKeywordScore(),
                reviewScore.getFeedback(),
                reviewScore.getSuggestions()
        );
    }
}

package com.resumereview.controller;

import com.resumereview.model.ReviewScore;
import com.resumereview.dto.ReviewScoreDTO;
import com.resumereview.service.ReviewScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review-scores")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReviewScoreController {

    @Autowired
    private ReviewScoreService reviewScoreService;

    /**
     * Generate review score for a resume
     */
    @PostMapping("/generate/{resumeId}")
    public ResponseEntity<ReviewScoreDTO> generateReviewScore(@PathVariable Long resumeId) {
        log.info("Generating review score for resume ID: {}", resumeId);
        
        ReviewScore reviewScore = reviewScoreService.generateReviewScore(resumeId);
        ReviewScoreDTO dto = reviewScoreService.convertToDTO(reviewScore);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Get review score for a resume
     */
    @GetMapping("/resume/{resumeId}")
    public ResponseEntity<ReviewScoreDTO> getReviewScore(@PathVariable Long resumeId) {
        log.info("Fetching review score for resume ID: {}", resumeId);
        
        ReviewScore reviewScore = reviewScoreService.getReviewScore(resumeId);
        ReviewScoreDTO dto = reviewScoreService.convertToDTO(reviewScore);
        
        return ResponseEntity.ok(dto);
    }

    /**
     * Get review score by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewScoreDTO> getReviewScoreById(@PathVariable Long id) {
        log.info("Fetching review score with ID: {}", id);
        
        // Note: In a real application, you'd have a proper repository method for this
        // For now, this is a placeholder
        return ResponseEntity.ok(new ReviewScoreDTO());
    }
}

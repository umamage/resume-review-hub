package com.resumereview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private Double overallScore;

    @Column(nullable = false)
    private Double formatScore;

    @Column(nullable = false)
    private Double contentScore;

    @Column(nullable = false)
    private Double keywordScore;

    @Column(columnDefinition = "LONGTEXT")
    private String feedback;

    @Column(columnDefinition = "LONGTEXT")
    private String suggestions;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

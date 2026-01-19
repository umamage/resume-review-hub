package com.resumereview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String company;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(nullable = false)
    private Double matchScore;

    @Column(length = 500)
    private String location;

    @Column(length = 50)
    private String employmentType;

    @Column(length = 1000)
    private String requiredSkills;

    @Column(length = 500)
    private String jobUrl;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private LocalDateTime suggestedAt;

    @Column(length = 20)
    private String status;
}

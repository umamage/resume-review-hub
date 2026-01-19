package com.resumereview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_suggestion_id", nullable = false)
    private JobSuggestion jobSuggestion;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(columnDefinition = "LONGTEXT")
    private String applicationNotes;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @Column
    private LocalDateTime responseDate;

    @Column(length = 50)
    private String responseStatus;

    @Column(columnDefinition = "LONGTEXT")
    private String responseMessage;
}

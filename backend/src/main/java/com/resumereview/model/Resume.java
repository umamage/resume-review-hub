package com.resumereview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(columnDefinition = "LONGTEXT")
    private String extractedText;

    @OneToOne(mappedBy = "resume", cascade = CascadeType.ALL)
    private ReviewScore reviewScore;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 50)
    private String status;
}

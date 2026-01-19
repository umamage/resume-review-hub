package com.resumereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewScoreDTO {
    private Long id;
    private Double overallScore;
    private Double formatScore;
    private Double contentScore;
    private Double keywordScore;
    private String feedback;
    private String suggestions;
}

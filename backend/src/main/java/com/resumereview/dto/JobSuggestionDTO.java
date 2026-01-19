package com.resumereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSuggestionDTO {
    private Long id;
    private String jobTitle;
    private String company;
    private String description;
    private Double matchScore;
    private String location;
    private String employmentType;
    private String requiredSkills;
    private String jobUrl;
    private String status;
}

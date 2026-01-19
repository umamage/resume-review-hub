package com.resumereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {
    private Long id;
    private Long jobSuggestionId;
    private String jobTitle;
    private String company;
    private String status;
    private String applicationNotes;
    private String responseStatus;
}

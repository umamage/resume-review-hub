package com.resumereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeUploadResponse {
    private Long resumeId;
    private String fileName;
    private Long fileSize;
    private String message;
    private Boolean success;
}

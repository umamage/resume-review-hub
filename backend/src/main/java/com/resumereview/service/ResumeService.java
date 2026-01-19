package com.resumereview.service;

import com.resumereview.model.Resume;
import com.resumereview.repository.ResumeRepository;
import com.resumereview.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Value("${app.file-upload.directory}")
    private String uploadDirectory;

    @Value("${app.resume.max-file-size}")
    private Long maxFileSize;

    /**
     * Upload a resume file and extract text
     */
    public Resume uploadResume(MultipartFile file) throws IOException {
        log.info("Starting resume upload for file: {}", file.getOriginalFilename());

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }

        // Create uploads directory if it doesn't exist
        File uploadsDir = new File(uploadDirectory);
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs();
        }

        // Generate unique filename
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadsDir, uniqueFileName);

        // Save file
        file.transferTo(destinationFile);
        log.info("File saved successfully to: {}", destinationFile.getAbsolutePath());

        // Extract text from PDF
        String extractedText = extractTextFromPdf(destinationFile);

        // Create and save resume entity
        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setFilePath(destinationFile.getAbsolutePath());
        resume.setFileSize(file.getSize());
        resume.setExtractedText(extractedText);
        resume.setUploadedAt(LocalDateTime.now());
        resume.setUpdatedAt(LocalDateTime.now());
        resume.setStatus("UPLOADED");

        Resume savedResume = resumeRepository.save(resume);
        log.info("Resume saved with ID: {}", savedResume.getId());

        return savedResume;
    }

    /**
     * Extract text from PDF file
     */
    private String extractTextFromPdf(File pdfFile) {
        try {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();
            return text;
        } catch (IOException e) {
            log.error("Error extracting text from PDF: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Get resume by ID
     */
    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + id));
    }

    /**
     * Get all resumes
     */
    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    /**
     * Delete resume
     */
    public void deleteResume(Long id) {
        Resume resume = getResumeById(id);
        
        // Delete file from disk
        File file = new File(resume.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        // Delete from database
        resumeRepository.deleteById(id);
        log.info("Resume deleted with ID: {}", id);
    }

    /**
     * Get resume status
     */
    public String getResumeStatus(Long id) {
        Resume resume = getResumeById(id);
        return resume.getStatus();
    }

    /**
     * Update resume status
     */
    public Resume updateResumeStatus(Long id, String status) {
        Resume resume = getResumeById(id);
        resume.setStatus(status);
        resume.setUpdatedAt(LocalDateTime.now());
        return resumeRepository.save(resume);
    }
}

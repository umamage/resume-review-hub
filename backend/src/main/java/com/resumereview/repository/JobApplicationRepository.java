package com.resumereview.repository;

import com.resumereview.model.JobApplication;
import com.resumereview.model.JobSuggestion;
import com.resumereview.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByResumeOrderByAppliedAtDesc(Resume resume);
    List<JobApplication> findByResumeAndStatus(Resume resume, String status);
    Optional<JobApplication> findByJobSuggestionAndResume(JobSuggestion jobSuggestion, Resume resume);
}

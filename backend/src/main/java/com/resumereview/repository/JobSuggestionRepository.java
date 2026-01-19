package com.resumereview.repository;

import com.resumereview.model.JobSuggestion;
import com.resumereview.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSuggestionRepository extends JpaRepository<JobSuggestion, Long> {
    List<JobSuggestion> findByResumeOrderByMatchScoreDesc(Resume resume);
    List<JobSuggestion> findByResumeAndStatus(Resume resume, String status);
}

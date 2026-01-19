package com.resumereview.repository;

import com.resumereview.model.ReviewScore;
import com.resumereview.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewScoreRepository extends JpaRepository<ReviewScore, Long> {
    Optional<ReviewScore> findByResume(Resume resume);
}

package com.example.talent_explorer.repository;

import com.example.talent_explorer.model.EducationPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationPredictionRepository extends JpaRepository<EducationPrediction, Long> {
    EducationPrediction findByTaskId(String taskId);
}

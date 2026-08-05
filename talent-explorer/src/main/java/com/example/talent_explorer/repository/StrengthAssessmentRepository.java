package com.example.talent_explorer.repository;

import com.example.talent_explorer.model.StrengthAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrengthAssessmentRepository extends JpaRepository<StrengthAssessment, Long> {
    StrengthAssessment findByTaskId(String taskId);
}

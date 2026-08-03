package com.example.talent_explorer.repository;

import com.example.talent_explorer.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByTaskId(String taskId);
}
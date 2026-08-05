package com.example.talent_explorer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "career_plans")
public class CareerPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reportId;
    private String taskId;
    private String userId;
    @Column(columnDefinition = "json")
    private String careerPathsJson;
    @Column(columnDefinition = "json")
    private String shortTermPlanJson;
    @Column(columnDefinition = "json")
    private String longTermPlanJson;
    @Column(columnDefinition = "text")
    private String planningSummary;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCareerPathsJson() { return careerPathsJson; }
    public void setCareerPathsJson(String careerPathsJson) { this.careerPathsJson = careerPathsJson; }
    public String getShortTermPlanJson() { return shortTermPlanJson; }
    public void setShortTermPlanJson(String shortTermPlanJson) { this.shortTermPlanJson = shortTermPlanJson; }
    public String getLongTermPlanJson() { return longTermPlanJson; }
    public void setLongTermPlanJson(String longTermPlanJson) { this.longTermPlanJson = longTermPlanJson; }
    public String getPlanningSummary() { return planningSummary; }
    public void setPlanningSummary(String planningSummary) { this.planningSummary = planningSummary; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

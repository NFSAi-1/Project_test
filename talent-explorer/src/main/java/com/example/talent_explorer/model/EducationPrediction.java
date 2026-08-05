package com.example.talent_explorer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "education_predictions")
public class EducationPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reportId;
    private String taskId;
    private String userId;
    private String predictedDegree;
    private Integer degreeConfidence;
    @Column(columnDefinition = "json")
    private String subjectDirectionsJson;
    @Column(columnDefinition = "text")
    private String educationAnalysis;
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
    public String getPredictedDegree() { return predictedDegree; }
    public void setPredictedDegree(String predictedDegree) { this.predictedDegree = predictedDegree; }
    public Integer getDegreeConfidence() { return degreeConfidence; }
    public void setDegreeConfidence(Integer degreeConfidence) { this.degreeConfidence = degreeConfidence; }
    public String getSubjectDirectionsJson() { return subjectDirectionsJson; }
    public void setSubjectDirectionsJson(String subjectDirectionsJson) { this.subjectDirectionsJson = subjectDirectionsJson; }
    public String getEducationAnalysis() { return educationAnalysis; }
    public void setEducationAnalysis(String educationAnalysis) { this.educationAnalysis = educationAnalysis; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

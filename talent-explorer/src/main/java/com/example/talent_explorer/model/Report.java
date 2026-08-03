package com.example.talent_explorer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskId;
    private String userId;
    private Integer openness;
    private Integer conscientiousness;
    private Integer extraversion;
    private Integer stability;
    @Column(columnDefinition = "json")
    private String tagsJson;
    private Integer validityScore;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Integer getOpenness() { return openness; }
    public void setOpenness(Integer openness) { this.openness = openness; }
    public Integer getConscientiousness() { return conscientiousness; }
    public void setConscientiousness(Integer conscientiousness) { this.conscientiousness = conscientiousness; }
    public Integer getExtraversion() { return extraversion; }
    public void setExtraversion(Integer extraversion) { this.extraversion = extraversion; }
    public Integer getStability() { return stability; }
    public void setStability(Integer stability) { this.stability = stability; }
    public String getTagsJson() { return tagsJson; }
    public void setTagsJson(String tagsJson) { this.tagsJson = tagsJson; }
    public Integer getValidityScore() { return validityScore; }
    public void setValidityScore(Integer validityScore) { this.validityScore = validityScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
package com.example.talent_explorer.model;

import java.util.List;
import java.util.Map;

public class AnswerRequest {
    private String userId;
    private List<AnswerItem> answers;
    private Map<String, Integer> dimensionScores;

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<AnswerItem> getAnswers() { return answers; }
    public void setAnswers(List<AnswerItem> answers) { this.answers = answers; }
    public Map<String, Integer> getDimensionScores() { return dimensionScores; }
    public void setDimensionScores(Map<String, Integer> dimensionScores) { this.dimensionScores = dimensionScores; }
}
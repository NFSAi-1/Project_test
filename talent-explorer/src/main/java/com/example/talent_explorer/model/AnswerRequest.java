package com.example.talent_explorer.model;

import java.util.List;

public class AnswerRequest {
    private String userId;
    private List<AnswerItem> answers;

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<AnswerItem> getAnswers() { return answers; }
    public void setAnswers(List<AnswerItem> answers) { this.answers = answers; }
}
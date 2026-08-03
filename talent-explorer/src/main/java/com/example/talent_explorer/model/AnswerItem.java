package com.example.talent_explorer.model;

public class AnswerItem {
    private Integer questionId;
    private Integer score;
    private Long responseTime;

    // Getters and Setters
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Long getResponseTime() { return responseTime; }
    public void setResponseTime(Long responseTime) { this.responseTime = responseTime; }
}
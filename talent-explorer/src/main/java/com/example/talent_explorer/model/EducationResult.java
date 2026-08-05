package com.example.talent_explorer.model;

import java.util.List;

public class EducationResult {
    private String predictedDegree;
    private Integer degreeConfidence;
    private List<SubjectDirection> subjectDirections;
    private String educationAnalysis;

    public String getPredictedDegree() { return predictedDegree; }
    public void setPredictedDegree(String predictedDegree) { this.predictedDegree = predictedDegree; }
    public Integer getDegreeConfidence() { return degreeConfidence; }
    public void setDegreeConfidence(Integer degreeConfidence) { this.degreeConfidence = degreeConfidence; }
    public List<SubjectDirection> getSubjectDirections() { return subjectDirections; }
    public void setSubjectDirections(List<SubjectDirection> subjectDirections) { this.subjectDirections = subjectDirections; }
    public String getEducationAnalysis() { return educationAnalysis; }
    public void setEducationAnalysis(String educationAnalysis) { this.educationAnalysis = educationAnalysis; }

    public static class SubjectDirection {
        private String direction;
        private Integer score;
        private List<String> subjects;

        public String getDirection() { return direction; }
        public void setDirection(String direction) { this.direction = direction; }
        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public List<String> getSubjects() { return subjects; }
        public void setSubjects(List<String> subjects) { this.subjects = subjects; }
    }
}

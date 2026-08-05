package com.example.talent_explorer.model;

import java.util.List;

public class PlanningResult {
    private List<CareerPath> careerPaths;
    private List<PlanPhase> shortTermPlan;
    private List<PlanPhase> longTermPlan;
    private String planningSummary;

    public List<CareerPath> getCareerPaths() { return careerPaths; }
    public void setCareerPaths(List<CareerPath> careerPaths) { this.careerPaths = careerPaths; }
    public List<PlanPhase> getShortTermPlan() { return shortTermPlan; }
    public void setShortTermPlan(List<PlanPhase> shortTermPlan) { this.shortTermPlan = shortTermPlan; }
    public List<PlanPhase> getLongTermPlan() { return longTermPlan; }
    public void setLongTermPlan(List<PlanPhase> longTermPlan) { this.longTermPlan = longTermPlan; }
    public String getPlanningSummary() { return planningSummary; }
    public void setPlanningSummary(String planningSummary) { this.planningSummary = planningSummary; }

    public static class CareerPath {
        private String career;
        private Integer matchScore;
        private String reason;

        public String getCareer() { return career; }
        public void setCareer(String career) { this.career = career; }
        public Integer getMatchScore() { return matchScore; }
        public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class PlanPhase {
        private String phase;
        private List<String> actions;
        private String timeline;

        public String getPhase() { return phase; }
        public void setPhase(String phase) { this.phase = phase; }
        public List<String> getActions() { return actions; }
        public void setActions(List<String> actions) { this.actions = actions; }
        public String getTimeline() { return timeline; }
        public void setTimeline(String timeline) { this.timeline = timeline; }
    }
}

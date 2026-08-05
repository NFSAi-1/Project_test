package com.example.talent_explorer.model;

import java.util.List;

public class StrengthResult {
    private List<DimensionItem> dimensions;
    private List<StrengthItem> strengths;
    private List<StrengthItem> weaknesses;
    private String overallSummary;

    public List<DimensionItem> getDimensions() { return dimensions; }
    public void setDimensions(List<DimensionItem> dimensions) { this.dimensions = dimensions; }
    public List<StrengthItem> getStrengths() { return strengths; }
    public void setStrengths(List<StrengthItem> strengths) { this.strengths = strengths; }
    public List<StrengthItem> getWeaknesses() { return weaknesses; }
    public void setWeaknesses(List<StrengthItem> weaknesses) { this.weaknesses = weaknesses; }
    public String getOverallSummary() { return overallSummary; }
    public void setOverallSummary(String overallSummary) { this.overallSummary = overallSummary; }

    public static class DimensionItem {
        private String name;
        private Integer score;
        private Integer percentile;
        private String level;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public Integer getPercentile() { return percentile; }
        public void setPercentile(Integer percentile) { this.percentile = percentile; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
    }

    public static class StrengthItem {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}

package com.example.talent_explorer.model;

import java.util.Map;
import java.util.List;

public class ReportResult {
    private Map<String, Integer> dimensionScores;
    private Map<String, Integer> percentileRanks;
    private List<String> tags;
    private List<String> redFlags;
    private Integer validityScore;

    // Getters and Setters
    public Map<String, Integer> getDimensionScores() { return dimensionScores; }
    public void setDimensionScores(Map<String, Integer> dimensionScores) { this.dimensionScores = dimensionScores; }
    public Map<String, Integer> getPercentileRanks() { return percentileRanks; }
    public void setPercentileRanks(Map<String, Integer> percentileRanks) { this.percentileRanks = percentileRanks; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public List<String> getRedFlags() { return redFlags; }
    public void setRedFlags(List<String> redFlags) { this.redFlags = redFlags; }
    public Integer getValidityScore() { return validityScore; }
    public void setValidityScore(Integer validityScore) { this.validityScore = validityScore; }
}
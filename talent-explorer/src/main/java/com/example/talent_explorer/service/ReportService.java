package com.example.talent_explorer.service;

import com.example.talent_explorer.model.AnswerRequest;
import com.example.talent_explorer.model.ReportResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ReportService {
    private final AnalysisService analysisService;

    public ReportService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public Mono<ReportResult> generateReport(String userId, AnswerRequest request) {
        String taskId = UUID.randomUUID().toString();
        request.setUserId(userId);
        return analysisService.runFullAnalysis(userId, taskId, request);
    }
}
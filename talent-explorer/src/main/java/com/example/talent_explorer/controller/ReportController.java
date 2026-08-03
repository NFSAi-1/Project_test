package com.example.talent_explorer.controller;

import com.example.talent_explorer.model.AnswerRequest;
import com.example.talent_explorer.model.ReportResult;
import com.example.talent_explorer.service.ReportService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/submit")
    public Mono<ReportResult> submit(@RequestBody AnswerRequest request) {
        String userId = "temp_user";
        return reportService.generateReport(userId, request);
    }
}
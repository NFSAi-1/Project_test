package com.example.talent_explorer.service;

import com.example.talent_explorer.model.AnswerRequest;
import com.example.talent_explorer.model.ReportResult;
import com.example.talent_explorer.repository.ReportRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReportService {
    private final PythonClient pythonClient;
    private final ReportRepository reportRepository;

    public ReportService(PythonClient pythonClient, ReportRepository reportRepository) {
        this.pythonClient = pythonClient;
        this.reportRepository = reportRepository;
    }

    public Mono<ReportResult> generateReport(String userId, AnswerRequest request) {
        request.setUserId(userId);
        return pythonClient.compute(request)
                .doOnNext(result -> {
                    // 此处可添加保存到数据库的逻辑
                    System.out.println("报告生成成功: " + userId);
                });
    }
}
package com.example.talent_explorer.service;

import com.example.talent_explorer.model.AnswerRequest;
import com.example.talent_explorer.model.EducationResult;
import com.example.talent_explorer.model.PlanningResult;
import com.example.talent_explorer.model.ReportResult;
import com.example.talent_explorer.model.StrengthResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class PythonClient {
    private static final Logger log = LoggerFactory.getLogger(PythonClient.class);
    private final WebClient webClient;
    private final String pythonUrl;
    private final String educationUrl;
    private final String strengthsUrl;
    private final String planningUrl;

    public PythonClient(WebClient webClient,
                        @Value("${python.api.url}") String pythonUrl,
                        @Value("${python.api.education-url}") String educationUrl,
                        @Value("${python.api.strengths-url}") String strengthsUrl,
                        @Value("${python.api.planning-url}") String planningUrl) {
        this.webClient = webClient;
        this.pythonUrl = pythonUrl;
        this.educationUrl = educationUrl;
        this.strengthsUrl = strengthsUrl;
        this.planningUrl = planningUrl;
    }

    public Mono<ReportResult> compute(AnswerRequest request) {
        return webClient.post()
                .uri(pythonUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ReportResult.class)
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(e -> {
                    log.error("Python 计算服务调用失败 (url={}): {}", pythonUrl, e.toString());
                    return Mono.just(getFallbackResult(request));
                });
    }

    private ReportResult getFallbackResult(AnswerRequest request) {
        ReportResult result = new ReportResult();
        result.setDimensionScores(Map.of(
                "openness", 50, "conscientiousness", 50,
                "extraversion", 50, "stability", 50
        ));
        result.setTags(List.of("需要重新测试"));
        result.setRedFlags(List.of("服务暂时不可用，请稍后重试"));
        result.setValidityScore(0);
        return result;
    }

    public Mono<EducationResult> analyzeEducation(AnswerRequest request) {
        return webClient.post()
                .uri(educationUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EducationResult.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.error("教育分析服务调用失败 (url={}): {}", educationUrl, e.toString());
                    return Mono.just(getFallbackEducation());
                });
    }

    public Mono<StrengthResult> analyzeStrengths(AnswerRequest request) {
        return webClient.post()
                .uri(strengthsUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StrengthResult.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.error("优势评估服务调用失败 (url={}): {}", strengthsUrl, e.toString());
                    return Mono.just(getFallbackStrengths());
                });
    }

    public Mono<PlanningResult> analyzePlanning(AnswerRequest request) {
        return webClient.post()
                .uri(planningUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PlanningResult.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.error("规划建议服务调用失败 (url={}): {}", planningUrl, e.toString());
                    return Mono.just(getFallbackPlanning());
                });
    }

    private EducationResult getFallbackEducation() {
        EducationResult result = new EducationResult();
        result.setPredictedDegree("数据不足");
        result.setDegreeConfidence(0);
        result.setSubjectDirections(List.of());
        result.setEducationAnalysis("分析服务暂时不可用，请稍后重试");
        return result;
    }

    private StrengthResult getFallbackStrengths() {
        StrengthResult result = new StrengthResult();
        result.setDimensions(List.of());
        result.setStrengths(List.of());
        result.setWeaknesses(List.of());
        result.setOverallSummary("分析服务暂时不可用，请稍后重试");
        return result;
    }

    private PlanningResult getFallbackPlanning() {
        PlanningResult result = new PlanningResult();
        result.setCareerPaths(List.of());
        result.setShortTermPlan(List.of());
        result.setLongTermPlan(List.of());
        result.setPlanningSummary("分析服务暂时不可用，请稍后重试");
        return result;
    }
}
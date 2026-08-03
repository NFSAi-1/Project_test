package com.example.talent_explorer.service;

import com.example.talent_explorer.model.AnswerRequest;
import com.example.talent_explorer.model.ReportResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class PythonClient {
    private final WebClient webClient;
    private final String pythonUrl;

    public PythonClient(WebClient webClient, @Value("${python.api.url}") String pythonUrl) {
        this.webClient = webClient;
        this.pythonUrl = pythonUrl;
    }

    public Mono<ReportResult> compute(AnswerRequest request) {
        return webClient.post()
                .uri(pythonUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ReportResult.class)
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(e -> Mono.just(getFallbackResult(request)));
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
}
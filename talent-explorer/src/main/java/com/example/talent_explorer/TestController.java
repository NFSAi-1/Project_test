package com.example.talent_explorer;

import com.example.talent_explorer.model.AnswerItem;
import com.example.talent_explorer.model.AnswerRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    private final WebClient webClient;
    private final String pythonUrl;

    public TestController(WebClient webClient, @Value("${python.api.url}") String pythonUrl) {
        this.webClient = webClient;
        this.pythonUrl = pythonUrl;
    }

    @GetMapping("/test-python")
    public Mono<String> testPython() {
        List<AnswerItem> answers = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            AnswerItem item = new AnswerItem();
            item.setQuestionId(i);
            item.setScore(i <= 20 ? 3 : 2);
            item.setResponseTime(2000L);
            answers.add(item);
        }
        AnswerRequest request = new AnswerRequest();
        request.setUserId("test_user");
        request.setAnswers(answers);

        return webClient.post()
                .uri(pythonUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Python 服务连接失败: " + e.getMessage()));
    }
}
package com.example.talent_explorer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    private final WebClient webClient;

    // 直接注入 WebClient，不再用 Builder
    public TestController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/test-python")
    public Mono<String> testPython() {
        return webClient.post()
                .uri("http://localhost:8000/compute")  // 直接写完整地址
                .retrieve()
                .bodyToMono(String.class);
    }
}
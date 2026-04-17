package com.rememberme.dunoesanchaeg.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public String healthCheck() {
        // 서버가 정상 작동 중임을 알리는 간단한 응답
        return "Dunoesanchaeg API Server is running!";
    }
}

package com.remember.remember_me_v1_api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 로그인 성공 후 자동으로 이동될 기본 경로
    @GetMapping("/")
    public String home() {
        return "<h1>카카오 로그인 및 DB 적재 테스트 성공!</h1><p>데이터베이스(RDS)를 확인해 보세요.</p>";
    }
}

package com.rememberme.dunoesanchaeg.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // 에러가 발생하는 boot.http.client 패키지 대신
                // spring.http.client 패키지의 표준 팩토리를 사용합니다.
                // 빨간줄 무시해도 됨
                .requestFactory(SimpleClientHttpRequestFactory::new)
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}
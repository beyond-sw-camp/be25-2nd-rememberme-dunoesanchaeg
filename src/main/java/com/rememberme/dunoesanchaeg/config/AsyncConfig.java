package com.rememberme.dunoesanchaeg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "anomalyDetectionTaskExecutor")
    public Executor anomalyDetectionTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);        // 기본적으로 실행 대기 중인 스레드 수
        executor.setMaxPoolSize(10);        // 동시 동작하는 최대 스레드 수
        executor.setQueueCapacity(50);      // MaxPoolSize를 초과하는 요청이 큐에 저장될 최대 개수
        executor.setThreadNamePrefix("AnomalyDetect-");
        executor.initialize();
        return executor;
    }
}

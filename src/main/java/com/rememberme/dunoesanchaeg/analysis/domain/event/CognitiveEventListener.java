package com.rememberme.dunoesanchaeg.analysis.domain.event;

import com.rememberme.dunoesanchaeg.analysis.service.AnomalyDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CognitiveEventListener {

    private final AnomalyDetectionService anomalyDetectionService;

    // 로그 저장이 완료된 직후 이벤트를 수신하여 비동기 스레드에서 인지 능력 이상 여부를 판단
    @Async("anomalyDetectionTaskExecutor")
    @EventListener
    public void handleCognitiveEvent(CognitiveEvent event) {
        log.info("[Event Listener] 비동기 처리 시작 - MemberId: {}, Scope: {}", 
                 event.getMemberId(), event.getMetricScope());
        
        try {
            // 이벤트를 통해 전달받은 memberId와 분석 대상(게임,질문)을 기반으로 로직 수행
            anomalyDetectionService.analyzeAndTriggerAlert(event.getMemberId(), event.getMetricScope());
        } catch (Exception e) {
            log.error("[Event Listener] 예기치 못한 에러 발생: {}", e.getMessage(), e);
        }
    }
}

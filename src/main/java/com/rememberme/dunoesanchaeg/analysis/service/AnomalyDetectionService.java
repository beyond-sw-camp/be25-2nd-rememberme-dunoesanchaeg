package com.rememberme.dunoesanchaeg.analysis.service;

import com.rememberme.dunoesanchaeg.analysis.domain.AlertState;
import com.rememberme.dunoesanchaeg.analysis.domain.enums.AlertType;
import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import com.rememberme.dunoesanchaeg.analysis.dto.AnomalyLogDto;
import com.rememberme.dunoesanchaeg.analysis.mapper.AnomalyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnomalyDetectionService {

    private final AnomalyMapper anomalyMapper;
    private final EmailService emailService;

    private static final int GAME_MIN_LOGS = 10;                // 게임 최소 로그 개수
    private static final int QUESTION_MIN_LOGS = 6;             // 질문 최소 로그 개수
    private static final int REQUIRE_GAME_RECENT_SIZE = 5;      // 필요한 게임 로그 개수
    private static final int REQUIRE_QUESTION_RECENT_SIZE = 3;  // 필요한 질문 로그 개수

    @Transactional
    public void analyzeAndTriggerAlert(Long memberId, MetricScope metricScope) {
        log.info("[이상 감지 분석 시작] memberId: {}, scope: {}", memberId, metricScope);

        // 최근 로그 추출
        List<AnomalyLogDto> recentLogs = fetchRecentLogs(memberId, metricScope);

        // 최소 데이터 건수 검증
        int requiredMinLogs = MetricScope.QUESTION.equals(metricScope) ? QUESTION_MIN_LOGS : GAME_MIN_LOGS;
        if (recentLogs.size() < requiredMinLogs) {
            log.info(" - 분석 보류: 데이터 부족 (현재: {}건, 필요: {}건)", recentLogs.size(), requiredMinLogs);
            return;
        }

        AlertType alertType = determineAlertType(metricScope);

        AlertState state = anomalyMapper.findAlertState(memberId, alertType, metricScope);
        if (state == null) {
            state = initAlertState(memberId, alertType, metricScope);
        }

        // 장기 휴면 복귀 여부 확인 (7일 이상 경과했으면 스택 0으로 초기화)
        if (state.getLastActivityAt() != null && state.getLastActivityAt().isBefore(LocalDateTime.now().minusDays(7))) {
            log.info(" - 7일 이상 휴면: 스택 초기화");
            state = state.toBuilder().consecutiveCount(0).build();
        }

        // 활동 시간 업데이트
        state = state.toBuilder().lastActivityAt(LocalDateTime.now()).build();

        // 이상 여부 탐지
        boolean isAnomalyDetected = checkAnomaly(metricScope, recentLogs);

        // 스택 등락 반영
        if (isAnomalyDetected) {
            state = state.toBuilder().consecutiveCount(state.getConsecutiveCount() + 1).build();
            log.info(" - 이상 징후 포착! 현재 연속 횟수: {}", state.getConsecutiveCount());
        } else {
            state = state.toBuilder().consecutiveCount(0).build(); // 정상이면 리셋
            log.info(" - 정상 기록으로 판단되어 스택 리셋");
        }

        // 연속 2회 이상이면 이메일 알림 발송
        if (state.getConsecutiveCount() >= 2) {
            state = handleAlertTrigger(state);
        }

        // 변경된 스택 및 시간 기록
        anomalyMapper.upsertAlertState(state);
    }

    private List<AnomalyLogDto> fetchRecentLogs(Long memberId, MetricScope metricScope) {
        if (MetricScope.QUESTION.equals(metricScope)) {
            return anomalyMapper.findRecentQuestionLogs(memberId, QUESTION_MIN_LOGS);
        }
        return anomalyMapper.findRecentGameLogs(memberId, metricScope, GAME_MIN_LOGS);
    }

    private boolean checkAnomaly(MetricScope metricScope, List<AnomalyLogDto> logs) {
        if (MetricScope.QUESTION.equals(metricScope)) {
            // 개방형 질문 : 최근 3회 평균 응답속도 > (이전 3회 평균 응답속도 * 1.2)
            double recentAvg = calculateQuestionAvg(logs.subList(0, REQUIRE_QUESTION_RECENT_SIZE));
            double controlAvg = calculateQuestionAvg(logs.subList(REQUIRE_QUESTION_RECENT_SIZE, REQUIRE_QUESTION_RECENT_SIZE * 2)); // 4~6번째 과거 데이터
            return recentAvg > (controlAvg * 1.2);
        } else {
            // 미니게임 : 최근 5회 평균 정확도 < (이전 5회 평균 정확도 * 0.8)
            double recentAvg = calculateGameAccuracyAvg(logs.subList(0, REQUIRE_GAME_RECENT_SIZE));
            double controlAvg = calculateGameAccuracyAvg(logs.subList(REQUIRE_GAME_RECENT_SIZE, REQUIRE_GAME_RECENT_SIZE * 2)); // 6~10번째 과거 데이터
            return recentAvg < (controlAvg * 0.8);
        }
    }

    private double calculateGameAccuracyAvg(List<AnomalyLogDto> targetLogs) {
        return targetLogs.stream()
                .mapToDouble(AnomalyLogDto::getAccuracy)
                .average()
                .orElse(0.0);
    }

    private double calculateQuestionAvg(List<AnomalyLogDto> targetLogs) {
        return targetLogs.stream()
                .mapToInt(AnomalyLogDto::getResponseSecond)
                .average()
                .orElse(0.0);
    }

    private AlertState handleAlertTrigger(AlertState state) {
        // 쿨타임 정책 확인 (7일 이내 발송 기록이 있으면 스킵)
        if (state.getLastSentAt() != null && state.getLastSentAt().isAfter(LocalDateTime.now().minusDays(7))) {
            log.info(" - 알림 스킵 (쿨타임 미달): 마지막 발송일 {}", state.getLastSentAt());
            anomalyMapper.insertAlertHistory(
                    state.getMemberId(), state.getAlertType(), state.getMetricScope(),
                    "SKIPPED", "WITHIN_7_DAYS_COOLDOWN");
            return state;
        }

        try {
            // 이메일 발송
            emailService.sendAnomalyAlertToGuardian(state.getMemberId(), state.getMetricScope());

            // 발송 성공 처리 및 스택 리셋
            state = state.toBuilder()
                    .lastSentAt(LocalDateTime.now())
                    .consecutiveCount(0)
                    .build();
            
            anomalyMapper.insertAlertHistory(
                    state.getMemberId(), state.getAlertType(), state.getMetricScope(),
                    "SENT", null);

        } catch (Exception e) {
            log.error("알림 발송 중 에러 발생: {}", e.getMessage());
            anomalyMapper.insertAlertHistory(
                    state.getMemberId(), state.getAlertType(), state.getMetricScope(),
                    "FAILED", "EMAIL_SEND_ERROR");
        }
        
        return state;
    }

    private AlertType determineAlertType(MetricScope metricScope) {
        if (MetricScope.QUESTION.equals(metricScope)) {
            return AlertType.QUESTION_TIME_INCREASE;
        }
        return AlertType.GAME_ACCURACY_DROP;
    }

    private AlertState initAlertState(Long memberId, AlertType alertType, MetricScope metricScope) {
        return AlertState.builder()
                .memberId(memberId)
                .alertType(alertType)
                .metricScope(metricScope)
                .consecutiveCount(0)
                .build();
    }
}

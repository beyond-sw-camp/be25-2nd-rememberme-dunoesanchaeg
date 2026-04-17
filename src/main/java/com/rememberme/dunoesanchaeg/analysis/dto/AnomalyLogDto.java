package com.rememberme.dunoesanchaeg.analysis.dto;

import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AnomalyLogDto {
    private Long id;
    private Long memberId;
    private MetricScope logType; // WORD_MEMORY, ARITHMETIC, DESCARTES_RPS || QUESTION

    // 게임인 경우 사용
    private Integer totalTryCount;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer timeoutCount;
    private Integer totalPlayedTime;

    // 질문인 경우 사용
    private Integer responseSecond;

    private LocalDateTime createdAt;

    // 계산된 정확도
    public double getAccuracy() {
        if (totalTryCount == null || totalTryCount == 0)
            return 0.0;
        return (double) correctCount / totalTryCount;
    }
}

package com.rememberme.dunoesanchaeg.analysis.domain;

import com.rememberme.dunoesanchaeg.analysis.domain.enums.AlertType;
import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AlertState {
    private Long memberId;
    private AlertType alertType;
    private MetricScope metricScope;
    private Integer consecutiveCount;
    private LocalDateTime lastSentAt;
    private LocalDateTime lastActivityAt;
}

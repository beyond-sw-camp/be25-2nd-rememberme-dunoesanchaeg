package com.rememberme.dunoesanchaeg.analysis.domain;

import com.rememberme.dunoesanchaeg.analysis.domain.enums.AlertType;
import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlertState {
    private Long memberId;
    private AlertType alertType;
    private MetricScope metricScope;
    private Integer consecutiveCount;
    private LocalDateTime lastSentAt;
    private LocalDateTime lastActivityAt;
}

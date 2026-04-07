package com.rememberme.dunoesanchaeg.analysis.domain.event;

import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

@Getter
public class CognitiveEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long memberId;
    private final MetricScope metricScope; // WORD_MEMORY, ARITHMETIC, DESCARTES_RPS || QUESTION

    public CognitiveEvent(Object source, Long memberId, MetricScope metricScope) {
        super(source);
        this.memberId = memberId;
        this.metricScope = metricScope;
    }
}

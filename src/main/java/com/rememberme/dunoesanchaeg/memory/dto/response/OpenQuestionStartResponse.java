package com.rememberme.dunoesanchaeg.memory.dto.response;

import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OpenQuestionStartResponse {
    private final Long dailyQuestionLogId;

    private final Long questionId;

    private final String questionText;

    private final DailyQuestionLogStatus status;
}

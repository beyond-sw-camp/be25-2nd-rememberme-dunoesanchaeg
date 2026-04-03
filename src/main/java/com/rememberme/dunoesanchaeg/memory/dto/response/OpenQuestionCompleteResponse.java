package com.rememberme.dunoesanchaeg.memory.dto.response;

import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OpenQuestionCompleteResponse {
    private final Long dailyQuestionLogId;

    private final DailyQuestionLogStatus status;
}

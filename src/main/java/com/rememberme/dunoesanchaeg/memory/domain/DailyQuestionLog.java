package com.rememberme.dunoesanchaeg.memory.domain;

import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyQuestionLog {
    private Long dailyQuestionLogId;

    private Long memberId;

    private LocalDate recordDate;

    private Integer responseSecond;

    private Long questionId;

    private DailyQuestionLogStatus dailyQuestionLogStatus;

    private LocalDateTime createdAt;
}

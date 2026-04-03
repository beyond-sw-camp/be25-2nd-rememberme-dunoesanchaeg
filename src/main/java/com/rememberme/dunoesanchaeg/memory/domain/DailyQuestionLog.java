package com.rememberme.dunoesanchaeg.memory.domain;

import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyQuestionLog {
    private Long dailyQuestionLogId;

    private Long memberId;

    private LocalDate recordDate;

    private Integer responseSecond;

    private Long questionId;

    private DailyQuestionLogStatus status;

    private LocalDateTime createdAt;
}

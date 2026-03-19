package com.rememberme.dunoesanchaeg.memory.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyRecord {

    private Long dailyRecordId;
    private Long memberId;
    private LocalDate recordDate;

    private String moodLevel;
    private String moodMemo;

    private String sleepLevel;
    private String sleepMemo;

    private String mealLevel;
    private String mealMemo;

    private String exerciseLevel;
    private String exerciseMemo;

    private String socialLevel;
    private String socialMemo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.rememberme.dunoesanchaeg.memory.domain;

import com.rememberme.dunoesanchaeg.memory.domain.enums.Level;
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

    private Level moodLevel;
    private String moodMemo;

    private Level sleepLevel;
    private String sleepMemo;

    private Level mealLevel;
    private String mealMemo;

    private Level exerciseLevel;
    private String exerciseMemo;

    private Level socialLevel;
    private String socialMemo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

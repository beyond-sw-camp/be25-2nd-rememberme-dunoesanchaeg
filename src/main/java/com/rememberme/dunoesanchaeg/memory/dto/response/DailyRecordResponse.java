package com.rememberme.dunoesanchaeg.memory.dto.response;

import com.rememberme.dunoesanchaeg.memory.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyRecordResponse {
    private final LocalDate recordDate;

    private final Level moodLevel;
    private final String moodMemo;

    private final Level sleepLevel;
    private final String sleepMemo;

    private final Level mealLevel;
    private final String mealMemo;

    private final Level exerciseLevel;
    private final String exerciseMemo;

    private final Level socialLevel;
    private final String socialMemo;
}

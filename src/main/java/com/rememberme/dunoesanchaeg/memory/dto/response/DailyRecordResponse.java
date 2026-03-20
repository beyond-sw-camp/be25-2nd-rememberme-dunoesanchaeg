package com.rememberme.dunoesanchaeg.memory.dto.response;

import com.rememberme.dunoesanchaeg.memory.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyRecordResponse {
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
}

package com.rememberme.dunoesanchaeg.memory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyRecordResponse {
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
}

package com.rememberme.dunoesanchaeg.memory.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyRecordSaveRequest {

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

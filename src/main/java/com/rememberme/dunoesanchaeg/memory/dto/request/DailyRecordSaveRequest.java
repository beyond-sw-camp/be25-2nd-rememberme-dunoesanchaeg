package com.rememberme.dunoesanchaeg.memory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyRecordSaveRequest {
    @NotBlank(message = "moodLevel은 필수입니다.")
    private String moodLevel;
    private String moodMemo;

    @NotBlank(message = "sleepLevel은 필수입니다.")
    private String sleepLevel;
    private String sleepMemo;

    @NotBlank(message = "mealLevel은 필수입니다.")
    private String mealLevel;
    private String mealMemo;

    private String exerciseLevel;
    private String exerciseMemo;

    private String socialLevel;
    private String socialMemo;

}

package com.rememberme.dunoesanchaeg.memory.dto.request;

import com.rememberme.dunoesanchaeg.memory.domain.enums.Level;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyRecordSaveRequest {
    @NotBlank(message = "moodLevel은 필수입니다.")
    private Level moodLevel;
    private String moodMemo;

    @NotBlank(message = "sleepLevel은 필수입니다.")
    private Level sleepLevel;
    private String sleepMemo;

    @NotBlank(message = "mealLevel은 필수입니다.")
    private Level mealLevel;
    private String mealMemo;

    private Level exerciseLevel;
    private String exerciseMemo;

    private Level socialLevel;
    private String socialMemo;

}

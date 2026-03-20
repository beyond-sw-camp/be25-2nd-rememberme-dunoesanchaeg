package com.rememberme.dunoesanchaeg.memory.dto.request;

import com.rememberme.dunoesanchaeg.memory.domain.enums.Level;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailyRecordSaveRequest {
    @NotNull(message = "moodLevel은 필수입니다.")
    private Level moodLevel;
    private String moodMemo;

    @NotNull(message = "sleepLevel은 필수입니다.")
    private Level sleepLevel;
    private String sleepMemo;

    @NotNull(message = "mealLevel은 필수입니다.")
    private Level mealLevel;
    private String mealMemo;

    private Level exerciseLevel;
    private String exerciseMemo;

    private Level socialLevel;
    private String socialMemo;

}

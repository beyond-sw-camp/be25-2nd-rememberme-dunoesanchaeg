package com.rememberme.dunoesanchaeg.memory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenQuestionCompleteRequest {

    @NotNull
    @Min(value = 10, message = "10초 이상 생각한 후 답변해주세요.")
    private Integer responseSecond;
}

package com.rememberme.dunoesanchaeg.statistics.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticsRequest {

    @NotBlank(message = "필수 파라미터입니다. (YYYY-MM-DD 형식으로 입력해주세요.)")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "필수 파라미터입니다. (YYYY-MM-DD 형식으로 입력해주세요.)"
    )
    private String targetDate;
}
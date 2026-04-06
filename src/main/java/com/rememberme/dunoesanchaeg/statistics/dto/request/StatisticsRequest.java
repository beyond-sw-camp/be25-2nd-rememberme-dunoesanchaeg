package com.rememberme.dunoesanchaeg.statistics.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticsRequest {

    @NotBlank(message = "필수 파라미터입니다. (YYYY-MM-DD 형식으로 입력해주세요.)")
    private String targetDate;
}
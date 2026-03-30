package com.rememberme.dunoesanchaeg.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@NoArgsConstructor
public class KakaoLoginRequest {

    @NotBlank(message = "인가 코드는 필수입니다.")
    private String code; // accessToken 대신 code

}

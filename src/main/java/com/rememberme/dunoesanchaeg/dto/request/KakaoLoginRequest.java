package com.rememberme.dunoesanchaeg.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class KakaoLoginRequest {
    @NotBlank(message = "카카오 아이디가 누락되었습니다. 확인해주세요")
    private String kakaoId;

    @NotBlank(message = "이메일이 누락되었습니다. 확인해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

}

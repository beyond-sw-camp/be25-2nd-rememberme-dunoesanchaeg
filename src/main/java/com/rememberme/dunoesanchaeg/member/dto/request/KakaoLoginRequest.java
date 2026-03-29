package com.rememberme.dunoesanchaeg.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoLoginRequest {
    @NotBlank(message = "카카오가 발급하는 엑세스 토큰이 누락되었습니다. 확인해주세요")
    private String accessToken;

    @NotBlank(message = "이메일이 누락되었습니다. 확인해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

}

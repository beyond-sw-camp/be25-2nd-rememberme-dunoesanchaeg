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
    @NotBlank
    private String kakaoId;

    @NotBlank
    @Email
    private String email;

}

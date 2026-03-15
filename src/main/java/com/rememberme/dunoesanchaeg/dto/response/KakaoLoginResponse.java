package com.rememberme.dunoesanchaeg.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoLoginResponse {
    private Long memberId;

    @JsonProperty("isProfileCompleted")
    private boolean isProfileCompleted;

    private UserStatus userStatus;
    private FontSize fontSize;

    @JsonProperty("isHighContrast")
    private boolean isHighContrast;

    private String accessToken;
    private String refreshToken;
}

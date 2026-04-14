package com.rememberme.dunoesanchaeg.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class KakaoLoginResponse {
    Long memberId;

    String name;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;

    UserStatus userStatus;
    FontSize fontSize;

    @JsonProperty("isHighContrast")
    Boolean isHighContrast;

    String accessToken;

    @JsonIgnore
    String refreshToken;
}

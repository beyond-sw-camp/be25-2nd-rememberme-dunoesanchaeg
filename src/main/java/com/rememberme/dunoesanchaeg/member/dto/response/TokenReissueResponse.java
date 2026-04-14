package com.rememberme.dunoesanchaeg.member.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TokenReissueResponse {
    // 서비스에서 컨트롤러로 새로 만든 토큰 보낼 때 사용 (내부 전달용)

    String accessToken;
    String refreshToken;
    UserStatus userStatus;

    String name;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;
}

package com.rememberme.dunoesanchaeg.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RecoveryResponse {
    UserStatus userStatus;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;

    String accessToken;

    @JsonIgnore
    String refreshToken;
}

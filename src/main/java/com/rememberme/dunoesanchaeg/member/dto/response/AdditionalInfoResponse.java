package com.rememberme.dunoesanchaeg.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class AdditionalInfoResponse {
    Long memberId;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;

    UserStatus userStatus;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}

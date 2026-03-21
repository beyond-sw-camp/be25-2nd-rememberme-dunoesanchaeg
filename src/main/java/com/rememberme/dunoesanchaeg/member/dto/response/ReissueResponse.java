package com.rememberme.dunoesanchaeg.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;


@Builder
@Value
public class ReissueResponse {
    // accessToken만 response로 보내기 위해 새로 만듬
    // 컨트롤러가 프론트에 주기위함 (외부 전달용)

    String accessToken;
    UserStatus userStatus;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;
}

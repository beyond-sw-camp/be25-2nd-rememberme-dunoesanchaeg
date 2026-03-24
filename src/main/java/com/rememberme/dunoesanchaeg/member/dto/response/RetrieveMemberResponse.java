package com.rememberme.dunoesanchaeg.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.member.domain.enums.Role;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class RetrieveMemberResponse {
    String name;
    String email;
    String phone;
    FontSize fontSize;
    Role role;

    @JsonProperty("isHighContrast")
    Boolean isHighContrast;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;

    UserStatus userStatus;

    LocalDateTime deletedAt;
}

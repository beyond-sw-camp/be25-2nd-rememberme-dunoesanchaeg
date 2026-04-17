package com.rememberme.dunoesanchaeg.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Builder
@Value
public class UpdateMemberResponse {
    Long memberId;

    String name;

    String email;

    String phone;

    String birthDate;

    Boolean guardianConsent;

    String guardianEmail;

    String guardianPhone;

    FontSize fontSize;

    @JsonProperty("isHighContrast")
    Boolean isHighContrast;

    UserStatus userStatus;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;

    LocalDateTime updatedAt;

}

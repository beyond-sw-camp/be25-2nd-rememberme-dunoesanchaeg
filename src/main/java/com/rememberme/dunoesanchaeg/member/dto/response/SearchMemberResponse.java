package com.rememberme.dunoesanchaeg.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.member.domain.enums.Role;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class SearchMemberResponse {
    String name;
    String email;
    String phone;
    FontSize fontSize;
    Role role;

    String birthDate;

    @JsonProperty("isHighContrast")
    Boolean isHighContrast;

    @JsonProperty("isProfileCompleted")
    Boolean isProfileCompleted;

    UserStatus userStatus;

    @JsonProperty("guardianConsent")
    Boolean guardianConsent;

    String guardianEmail;
    String guardianPhone;


    LocalDateTime deletedAt;


    // 탈퇴 유예 회원용 - 복구 안내에 필요한 최소 정보만 제공
    public static SearchMemberResponse ofWithdrawn(Member member) {
        return SearchMemberResponse.builder()
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .userStatus(member.getUserStatus())
                .deletedAt(member.getDeletedAt())
                .role(member.getRole())
                .build();
    }
}

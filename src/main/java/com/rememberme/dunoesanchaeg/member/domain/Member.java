package com.rememberme.dunoesanchaeg.member.domain;


import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.member.domain.enums.Role;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member {
    private Long memberId;
    private String kakaoId;
    private String email;
    private Role role;
    private String name;
    private LocalDate birthDate;
    private String phone;
    private String guardianEmail;
    private String guardianPhone;
    private boolean guardianConsent;
    private boolean isProfileCompleted;
    private UserStatus userStatus;
    private boolean isHighContrast;
    private FontSize fontSize;
    private Integer totalRoutineCount;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void updateEmail(String email) {
        this.email = email;
    }

    public void completeProfile(String name,
                                 LocalDate birthDate,
                                 String phone,
                                 String guardianEmail,
                                 String guardianPhone,
                                 boolean guardianConsent,
                                 FontSize fontSize,
                                 boolean isHighContrast){
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.guardianEmail = guardianEmail;
        this.guardianPhone = guardianPhone;
        this.guardianConsent = guardianConsent;
        this.fontSize = fontSize;
        this.isHighContrast = isHighContrast;
        this.isProfileCompleted = true;

    }
}

package com.rememberme.dunoesanchaeg.domain;


import com.rememberme.dunoesanchaeg.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.domain.enums.Role;
import com.rememberme.dunoesanchaeg.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
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

}

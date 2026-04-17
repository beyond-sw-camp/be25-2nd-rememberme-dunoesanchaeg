package com.rememberme.dunoesanchaeg.member.domain;


import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.member.domain.enums.Role;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import com.rememberme.dunoesanchaeg.member.dto.request.UpdateMemberRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member {
    private Long memberId;
    private Long kakaoId;
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

    // UpdateMemberRequest request로 들어온 값중 수정된 값만 변경
    public void patchProfile(UpdateMemberRequest request,
                      LocalDate validatedBirthDate,
                      String guardianEmail,
                      String guardianPhone,
                      boolean guardianConsent
    ){
        // 빈문자열만 검증

        if (StringUtils.hasText(request.getName())){
            this.name = request.getName();
        }

        if (StringUtils.hasText(request.getPhone())){
            this.phone = request.getPhone();
        }

        if (request.getFontSize() != null){
            this.fontSize = request.getFontSize();
        }

        if (request.getIsHighContrast() != null){
            this.isHighContrast = request.getIsHighContrast();
        }

        // 이미 검증됨
        this.birthDate = validatedBirthDate;
        this.guardianConsent = guardianConsent;
        this.guardianEmail = guardianEmail;
        this.guardianPhone = guardianPhone;
        this.isProfileCompleted = true;
    }

    // 계정탈퇴
    public void withdraw() {
        this.userStatus = UserStatus.WITHDRAWN;
        this.role = Role.WITHDRAWN;
        this.deletedAt = LocalDateTime.now();
    }

    // 계정 복구
    public void restore(){
        this.userStatus = UserStatus.ACTIVE;
        this.role = Role.USER;
        this.deletedAt = null;
    }
}

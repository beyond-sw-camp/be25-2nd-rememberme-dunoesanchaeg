package com.rememberme.dunoesanchaeg.member.dto.request;

import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberRequest {
    private String name;

    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다. (010XXXXXXXX)")
    private String phone;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 올바르지 않습니다. (YYYY-MM-DD)")
    private String birthDate;

    private Boolean guardianConsent;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String guardianEmail;

    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다. (010XXXXXXXX)")
    private String guardianPhone;

    private FontSize fontSize;

    private Boolean isHighContrast;

}

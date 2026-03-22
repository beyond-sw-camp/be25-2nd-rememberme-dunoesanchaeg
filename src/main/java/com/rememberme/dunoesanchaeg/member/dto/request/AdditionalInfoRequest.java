package com.rememberme.dunoesanchaeg.member.dto.request;

import com.rememberme.dunoesanchaeg.member.domain.enums.FontSize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalInfoRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 올바르지 않습니다. (YYYY-MM-DD)")
    @NotBlank(message = "생년월일을 입력해주세요")
    private String birthDate;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다. (010XXXXXXXX)")
    private String phone;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String guardianEmail;

    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다. (010XXXXXXXX)")
    private String guardianPhone;

    @NotNull(message = "보호자 활동 공유 동의 여부를 선택해주세요.")
    private Boolean guardianConsent;

    @NotNull(message = "글자 크기를 선택해주세요.")
    private FontSize fontSize;

    @NotNull(message = "고대비 모드 활성 여부를 선택해주세요.")
    private Boolean isHighContrast;

}

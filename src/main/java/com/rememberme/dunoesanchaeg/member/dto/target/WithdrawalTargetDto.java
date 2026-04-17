package com.rememberme.dunoesanchaeg.member.dto.target;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WithdrawalTargetDto {
    @NotNull(message = "멤버아이디가 누락되었습니다.")
    private Long memberId;

    @NotNull(message = "카카오 아이디가 누락되었습니다.")
    private Long kakaoId;
}
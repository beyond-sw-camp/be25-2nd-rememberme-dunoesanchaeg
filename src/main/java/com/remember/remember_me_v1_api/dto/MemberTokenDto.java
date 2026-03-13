package com.remember.remember_me_v1_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTokenDto {
    private Long id;
    private Long memberId;      // member 테이블의 PK
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
}
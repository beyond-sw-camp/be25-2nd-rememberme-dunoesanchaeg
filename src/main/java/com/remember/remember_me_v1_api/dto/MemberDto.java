package com.remember.remember_me_v1_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String provider;
    private String providerId;
    private String email; // 변경된 스코프 반영
    private String role;
    private String createdAt;
}

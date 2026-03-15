package com.rememberme.dunoesanchaeg.domain;


import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class MemberToken {
    private Long tokenId;
    private Long memberId;
    private String refreshToken;
    private String userAgent;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime lastUsedAt;
    private boolean isRevoked;

}

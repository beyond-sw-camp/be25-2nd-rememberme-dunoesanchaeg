package com.rememberme.dunoesanchaeg.common.security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public static final String REFRESH_TOKEN_NAME = "refreshToken";

    // 리프레시 토큰 전용 쿠키 생성
    public ResponseCookie createRefreshTokenCookie(String refreshToken, long maxAgeSeconds) {

        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // 배포 시 true(HTTPS 전용) 권장
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Lax")
                .build();
    }

    // 쿠키 삭제 (로그아웃 시 사용)
    public ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, "")
                .maxAge(0)
                .path("/")
                .build();
    }
}

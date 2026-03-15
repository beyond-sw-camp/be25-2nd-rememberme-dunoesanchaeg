package com.rememberme.dunoesanchaeg.service;

import com.rememberme.dunoesanchaeg.dto.response.KakaoLoginResponse;

public interface AuthService {
    KakaoLoginResponse kakaoAuth(
            String kakaoId,
            String email,
            String userAgent
    );
}

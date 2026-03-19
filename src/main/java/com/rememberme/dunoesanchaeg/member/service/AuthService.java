package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;


public interface AuthService {
    KakaoLoginResponse kakaoAuth(
            String kakaoId,
            String email,
            String userAgent
    );
}
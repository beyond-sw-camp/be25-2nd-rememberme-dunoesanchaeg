package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;


public interface AuthService {
    // 카카오 로그인
    KakaoLoginResponse kakaoAuth(
            String kakaoId,
            String email,
            String userAgent
    );
}
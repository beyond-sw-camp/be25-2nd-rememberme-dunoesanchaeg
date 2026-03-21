package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.TokenReissueResponse;


public interface AuthService {

    // 카카오 로그인
    KakaoLoginResponse kakaoAuth(
            String kakaoId,
            String email,
            String userAgent
    );

    // 토큰 재발급
    TokenReissueResponse reissue(String refreshToken, String userAgent);

}
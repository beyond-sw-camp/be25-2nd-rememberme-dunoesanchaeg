package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.TokenReissueResponse;


public interface AuthService {

    // 카카오 로그인
    KakaoLoginResponse kakaoAuth(
            String code,
            String userAgent
    );

    // 토큰 재발급
    TokenReissueResponse reissue(String refreshToken, String userAgent);

    // 로그아웃
    int logout(Long memberId, String userAgent);

    // 전체 로그아웃
    int logoutAll(Long memberId);

}
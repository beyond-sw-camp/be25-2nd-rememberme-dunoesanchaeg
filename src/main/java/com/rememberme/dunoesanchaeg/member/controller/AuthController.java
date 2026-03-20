package com.rememberme.dunoesanchaeg.member.controller;


import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.member.dto.request.KakaoLoginRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;
import com.rememberme.dunoesanchaeg.member.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/kakaoAuth")
    public ApiResponse<KakaoLoginResponse> loginWithKakao(
            @RequestHeader("User-Agent") String userAgent,
            @Valid @RequestBody KakaoLoginRequest kakaoLoginRequest,
            HttpServletResponse response
    ){
        KakaoLoginResponse kakaoLoginResponse = authService
                .kakaoAuth(kakaoLoginRequest.getKakaoId(),kakaoLoginRequest.getEmail(), userAgent);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", kakaoLoginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .maxAge(14 * 24 * 60 * 60) //14일
                .path("/")
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ApiResponse.success(200, "카카오 로그인 성공", kakaoLoginResponse);
    }


}

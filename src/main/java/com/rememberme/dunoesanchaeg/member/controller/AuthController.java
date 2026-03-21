package com.rememberme.dunoesanchaeg.member.controller;


import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.dto.request.KakaoLoginRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.ReissueResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.TokenReissueResponse;
import com.rememberme.dunoesanchaeg.member.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/reissue") //
    public ApiResponse<ReissueResponse> reissueToken(
            @CookieValue("refreshToken") String refreshToken,
            @Valid @RequestHeader("User-Agent") String userAgent,
            HttpServletResponse response){
        if(refreshToken == null){
            throw new BaseException(401,"세션이 만료되었거나 유효하지 않은 접근입니다.");
        }

        // 토큰 재발급 및 검증
        TokenReissueResponse reissue = authService.reissue(refreshToken, userAgent);

        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("refreshToken", reissue.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .maxAge(14 * 24 * 60 * 60) //14일
                .path("/")
                .sameSite("Strict")
                .build();

        // 쿠키 재설정
        response.addHeader("Set-Cookie", cookie.toString());

        // 응답 객체 생성
        ReissueResponse reissueResponse = ReissueResponse
                .builder()
                .accessToken(reissue.getAccessToken())
                .userStatus(reissue.getUserStatus())
                .isProfileCompleted(reissue.getIsProfileCompleted())
                .build();

        return ApiResponse.success(200,"토큰 재발급 성공", reissueResponse);
    }


}

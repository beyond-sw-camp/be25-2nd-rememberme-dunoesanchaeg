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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/kakaoAuth")
    public ResponseEntity<ApiResponse<KakaoLoginResponse>> loginWithKakao(
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

        return ResponseEntity.ok(ApiResponse.success(200, "카카오 로그인 성공", kakaoLoginResponse));
    }

    @PostMapping("/reissue") //
    public ResponseEntity<ApiResponse<ReissueResponse>> reissueToken(
            @CookieValue("refreshToken") String refreshToken,
            @Valid @RequestHeader("User-Agent") String userAgent,
            HttpServletResponse response)
    {
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

        return ResponseEntity.ok(ApiResponse.success(200,"토큰 재발급 성공", reissueResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal Long memberId,
            @RequestHeader("User-Agent") String userAgent,
            HttpServletResponse response

    ){
        int result = authService.logout(memberId, userAgent);
        if(result != 1){
            log.info("이미 로그아웃된 세션이거나 존재하지 않는 세션입니다. memberId: {}", memberId);
        }
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .maxAge(0) //즉시 만료
                .path("/")
                .sameSite("Strict")
                .build();

        // 쿠키 재설정
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(ApiResponse.success(200, "로그아웃 성공"));
    }

    @PostMapping("/logout/all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
            @AuthenticationPrincipal Long memberId,
            HttpServletResponse response

    ){
        int result = authService.logoutAll(memberId);
        if(result >0 ){
            log.info("[전체 로그아웃 성공] memberId: {}, 종료된 세션 수: {}", memberId, result);
        }else{
            log.info("[전체 로그아웃 알림] 이미 로그아웃된 세션이거나 존재하지 않는 세션입니다. memberId: {}", memberId);
        }
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .maxAge(0) //즉시 만료
                .path("/")
                .sameSite("Strict")
                .build();

        // 쿠키 재설정
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(ApiResponse.success(200, "전체 로그아웃 성공"));
    }

}

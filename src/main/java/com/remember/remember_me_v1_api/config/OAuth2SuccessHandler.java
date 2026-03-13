package com.remember.remember_me_v1_api.config;

import com.remember.remember_me_v1_api.dto.MemberDto;
import com.remember.remember_me_v1_api.dto.MemberTokenDto;
import com.remember.remember_me_v1_api.mapper.MemberMapper;
import com.remember.remember_me_v1_api.mapper.MemberTokenMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberMapper memberMapper;           // 회원 정보 조회용
    private final MemberTokenMapper memberTokenMapper; // 토큰 저장용

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String providerId = oAuth2User.getName();

        // 1. DB에서 회원 정보 조회 (member_id를 알아내기 위함)
        MemberDto member = memberMapper.findByProviderAndProviderId("kakao", providerId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 2. JWT 토큰 생성
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
        String email = (kakaoAccount != null) ? (String) kakaoAccount.get("email") : "unknown";
        String token = jwtTokenProvider.createToken(providerId, email);

        // 3. member_token 테이블에 저장
        MemberTokenDto tokenDto = MemberTokenDto.builder()
                .memberId(member.getId())
                .accessToken(token)
                .expiredAt(LocalDateTime.now().plusHours(1)) // 1시간 후 만료
                .build();

        memberTokenMapper.insertToken(tokenDto);

        // 4. 응답 출력
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\": \"success\", \"accessToken\": \"" + token + "\"}");
    }
}
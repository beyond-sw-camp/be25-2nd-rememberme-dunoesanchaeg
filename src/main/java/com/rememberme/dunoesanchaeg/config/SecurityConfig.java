package com.rememberme.dunoesanchaeg.config;

import com.rememberme.dunoesanchaeg.common.security.JwtFilter;
import com.rememberme.dunoesanchaeg.common.security.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화 (JWT를 사용하는 무상태성 API이므로 필수)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. 세션 정책: 세션을 사용하지 않음 (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 4. 인증/인가: 인증되지 않은 사용자가 접근했을 때 401과 함께 메시지 전달
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\": 401, \"message\": \"로그인이 필요한 서비스입니다.\"}");
                        })
                )
                // 5. 경로별 권한 제어
                .authorizeHttpRequests(auth -> auth
                        // 인증 없어도 접근 가능
                        .requestMatchers(
                                "/",
                                "/api/v1/auth/kakao-auth",  // 카카오 로그인 등 인증
                                "/api/v1/auth/reissue",       // 토큰 재발급
                                "/oauth2/**",                // OAuth2 리다이렉트 경로
                                "/v3/api-docs/**",           // Swagger용
                                "/swagger-ui/**"             // Swagger UI용
                        ).permitAll()
                        // 인증 필요
                        .requestMatchers(
                                "/api/v1/auth/logout/**",    // 로그아웃
                                "/api/v1/members/me",        // 프로필 조회 수정
                                "/api/v1/members/me/profile",   // 프로필 추가
                                "/api/v1/routines/**",       // 루틴 관련
                                "/api/v1/cognitive-games/**",// 미니게임
                                "/api/v1/open-questions/**", // 질문
                                "/api/v1/daily-records/**",  // 기록
                                "/api/v1/trophies",          // 트로피
                                "/api/v1/notices/**",        // 공지
                                "/api/v1/calendar/**",       // 캘린더
                                "/api/v1/statistics/**"     // 통계
                        ).hasRole("USER")
                        // ROLE_WITHDRAWN 만 복구 로직 접근 가능
                        .requestMatchers("/api/v1/members/me/recovery").hasRole("WITHDRAWN")
                        .anyRequest()
                        .authenticated()
                ) // JwtFilter는 스프링 시큐리티 내부에서만 사용
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Vue.js 개발 서버(Vite) 포트 허용
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization")); // 프론트에서 JWT 토큰을 읽을 수 있게 허용
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
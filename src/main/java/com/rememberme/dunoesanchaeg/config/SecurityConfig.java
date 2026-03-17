package com.rememberme.dunoesanchaeg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

                // 4. 경로별 권한 제어 (개발 편의를 위한 프리패스 설정)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/api/v1/auth/**",           // 카카오 로그인 등 인증
                                "/login/**", "/oauth2/**",   // OAuth2 리다이렉트 경로
                                "/api/test/**",              // 테스트용
                                "/api/v1/members/**",        // 회원 관련 (팀원 작업용)
                                "/api/v1/routines/**",       // 루틴 관련
                                "/api/v1/cognitive-games/**",// 미니게임
                                "/api/v1/open-questions/**", // 질문
                                "/api/v1/daily-records/**",  // 기록
                                "/api/v1/statistics/**",     // 통계
                                "/v3/api-docs/**",           // Swagger용
                                "/swagger-ui/**"             // Swagger UI용
                        ).permitAll()
                        .anyRequest().authenticated()    // 그 외는 토큰 필요 (나중에 잠글 예정)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Vue.js 개발 서버(Vite) 포트 허용
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization")); // 프론트에서 JWT 토큰을 읽을 수 있게 허용
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

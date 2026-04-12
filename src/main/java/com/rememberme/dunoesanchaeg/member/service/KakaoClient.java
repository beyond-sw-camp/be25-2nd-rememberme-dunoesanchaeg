package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.dto.target.KakaoTokenResponse;
import com.rememberme.dunoesanchaeg.member.dto.target.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoClient {
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.admin-key}")
    private String adminKey;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    KakaoTokenResponse.class
            );

            if (response.getBody() == null) {
                throw new BaseException(500, "카카오 토큰 응답이 비어있습니다.");
            }

            log.info("카카오 엑세스 토큰 획득 성공");
            return response.getBody().getAccessToken();

        } catch (HttpClientErrorException e) {
            log.error("카카오 토큰 요청 실패 - 상태 코드: {}, 바디: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BaseException(401, "카카오 인가 코드가 유효하지 않습니다.");
        }
    }

    public KakaoUserInfo getKakaoUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> body = response.getBody();
            if (body == null) throw new BaseException(500, "카카오 유저 정보 응답이 비어있습니다.");

            // 데이터 추출
            Long kakaoId = Long.valueOf(String.valueOf(body.get("id")));
            Object kakaoAccountObj = body.get("kakao_account");
            String email = null;

            if (kakaoAccountObj instanceof Map<?, ?> kakaoAccount) {
                email = (String) kakaoAccount.get("email");
            }

            log.info("카카오 유저 정보 획득 성공 - kakaoId: {}", kakaoId);
            return new KakaoUserInfo(kakaoId, email);

        } catch (HttpClientErrorException e) {
            log.error("카카오 유저 정보 요청 실패 - 상태 코드: {}, 바디: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BaseException(401, "유효하지 않은 카카오 토큰입니다.");
        }
    }

    public void logout(Long kakaoId) {
        String url = "https://kapi.kakao.com/v1/user/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", String.valueOf(kakaoId));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            log.warn("카카오 서버 로그아웃 호출 실패 (이미 만료되었을 수 있음): {}", e.getMessage());
        }
    }

    public void unlinkKakao(Long kakaoId) {
        // 1. 연동 해제 전용 URL
        String url = "https://kapi.kakao.com/v1/user/unlink";

        // 2. 헤더 설정 (Admin Key 방식)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 3. 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", String.valueOf(kakaoId));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            // 4. API 호출
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("카카오 연동 해제 성공 - kakaoId: {}", kakaoId);
            } else {
                log.error("카카오 연동 해제 응답 에러: {}", response.getStatusCode());
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // 에러 발생 시 상세 로그 (401 등 원인 파악용)
            log.error("카카오 연동 해제 실패 - 상태 코드: {}", e.getStatusCode());
            log.error("에러 바디: {}", e.getResponseBodyAsString());
            throw e; // 서비스 계층에서 알 수 있도록 예외처리
        } catch (Exception e) {
            log.error("카카오 연동 해제 중 알 수 없는 에러: {}", e.getMessage());
            throw e;
        }
    }
}

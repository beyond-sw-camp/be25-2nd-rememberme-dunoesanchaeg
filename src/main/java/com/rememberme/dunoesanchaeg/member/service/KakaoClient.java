package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.dto.target.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoClient {

    @Value("${kakao.admin-key}")
    private String adminKey;

    private final RestTemplate restTemplate;

    public KakaoUserInfo getKakaoUserInfo(String accessToken) {
        log.info("카카오로 보낼 토큰 확인: [{}]", accessToken);
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            // 1. URL은 동일하지만, 방식은 GET으로 바꿉니다. (카카오 권장)
            // restTemplate.exchange를 쓰면 응답 타입을 Map<String, Object>로 깔끔하게 받을 수 있습니다.
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    request,
                    new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> body = response.getBody();

            if (body == null) {
                throw new BaseException(500, "카카오 서버의 응답이 비어있습니다.");
            }

            // 2. 데이터 추출 (기존과 동일하지만 더 안전하게)
            Long kakaoId = Long.valueOf(String.valueOf(body.get("id")));

            // kakao_account 파싱
            Object kakaoAccountObj = body.get("kakao_account");
            String email = null;

            if (kakaoAccountObj instanceof Map<?, ?> kakaoAccount) {
                email = (String) kakaoAccount.get("email");
            }

            log.info("카카오 유저 정보 획득 성공 - kakaoId: {}", kakaoId);

            // 3. 결과 반환
            return new KakaoUserInfo(kakaoId, email);

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // 4. 에러 발생 시 로그를 아주 상세하게 찍도록 보강했습니다.
            log.error("카카오 API 호출 실패 - 상태 코드: {}", e.getStatusCode());
            log.error("에러 헤더 확인: {}", e.getResponseHeaders());
            log.error("에러 바디 확인: {}", e.getResponseBodyAsString());

            throw new BaseException(401, "카카오 인증에 실패했습니다.");
        }
    }

    public void logout(Long kakaoId) {
        String url = "https://kapi.kakao.com/v1/user/logout";

        HttpHeaders headers = new HttpHeaders();
        // 카카오 디벨로퍼 '앱 키' 탭에 있는 [Admin 키]를 넣으세요.
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
}

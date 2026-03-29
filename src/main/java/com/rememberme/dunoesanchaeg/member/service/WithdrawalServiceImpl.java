package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.target.WithdrawalTargetDto;
import com.rememberme.dunoesanchaeg.member.mapper.SchedulerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {
    private final SchedulerMapper schedulerMapper;
    private final RestTemplate restTemplate;

    @Value("${kakao.admin-key}")
    private String adminKey;

    @Override
    public void removeWithdrawnMembers() {
        List<WithdrawalTargetDto> withdrawalTList = schedulerMapper.selectWithdrawnMember();

        if(withdrawalTList.isEmpty()){
            log.info("탈퇴 처리 대상자가 없습니다.");
            return;
        }

        for (WithdrawalTargetDto target : withdrawalTList) {
            try {
                log.info("카카오톡 연동 해제 : " +
                        "memberId : {} , kakaoId : {}",
                        target.getMemberId(), target.getKakaoId());
                unlinkKakao(target.getKakaoId());
                schedulerMapper.updateKakaoUnlinkedStatus(target.getMemberId());
            }catch (Exception e) {
                log.error("카카오 API 호출 실패 - 카카오ID: {}, 메시지: {}, 원인: {}",
                        target.getKakaoId(), e.getMessage(), e.getClass().getSimpleName());
            }
        }
        int result = schedulerMapper.removeMemberPermanently();
        log.info("탈퇴 처리 결과 : {}", result);
        log.info("카카오톡 영구 탈퇴 처리 완료");

    }

    private void unlinkKakao(Long kakaoId) {

        // 1. 요청 URL 및 RestTemplate 준비
        String url = "https://kapi.kakao.com/v1/user/unlink";

        // 2. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 3. 바디 마라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", String.valueOf(kakaoId));

        // 4. 요청 객체 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 5. API 호출 실행
        try {
            // 주입받은 빈을 사용하므로 설정한 타임아웃이 적용됩니다.
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("카카오 API 호출 성공 - 카카오ID: {}", kakaoId);
            } else {
                throw new RuntimeException("카카오 API 응답 에러: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("카카오 API 호출 실패 - 카카오ID: {}, 사유: {}", kakaoId, e.getMessage());
            throw e;
        }
    }
}

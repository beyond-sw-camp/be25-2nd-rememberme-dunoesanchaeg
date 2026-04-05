package com.rememberme.dunoesanchaeg.analysis.service;

import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import com.rememberme.dunoesanchaeg.analysis.mapper.AnomalyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final AnomalyMapper anomalyMapper;

    public void sendAnomalyAlertToGuardian(Long memberId, MetricScope metricScope) {

        // 1. 보호자 이메일 및 동의 여부 DB에서 조회
        String guardianEmail = anomalyMapper.findGuardianEmail(memberId);

        // 2. 이메일이 등록되어 있지 않거나 동의하지 않은 경우 발송하지 않음
        if (guardianEmail == null || guardianEmail.trim().isEmpty()) {
            log.info("[알림 스킵] MemberID: {} - 등록된 보호자 메일이 없거나 수신에 동의하지 않았습니다.", memberId);
            return;
        }

        log.info("[알림 발송] MemberID: {}, 감지항목: {} - 보호자({})에게 이메일을 발송합니다.",
                memberId, metricScope, guardianEmail);

        // 3. 실제 이메일 발송 부분 (단순 텍스트 메일)
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(guardianEmail);
            message.setSubject("[두뇌산책] 보호자 알림 - 어르신 인지 기능 점검 안내");

            // 향후 MetricScope에 따라 상세 텍스트(ex: 게임점수 하락, 응답시간 증가)를 분기처리 하시면 좋습니다.
            message.setText("최근 미니게임/질문 답변 기록에서 평소와 다른 변화가 감지되었습니다.\n" +
                    "감지 항목: " + metricScope.name() + "\n" +
                    "앱에 접속하셔서 기록을 한 번 살펴봐 주세요.");

            mailSender.send(message);  // 메일 쏘기
            log.info("이메일 발송 성공!");

        } catch(MailException e) {
            log.error("이메일 발송 실패: {}", e.getMessage());
            // RuntimeException 발생시켜서 트랜잭션 밖의 로그에 FAILED로 담기게 유도할 수 있음
            throw new RuntimeException("이메일 발송 장애 발생");
        }
    }
}

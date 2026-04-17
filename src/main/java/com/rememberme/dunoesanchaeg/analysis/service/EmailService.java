package com.rememberme.dunoesanchaeg.analysis.service;

import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import com.rememberme.dunoesanchaeg.analysis.mapper.AnomalyMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final AnomalyMapper anomalyMapper;

    public void sendAnomalyAlertToGuardian(Long memberId, MetricScope metricScope) {

        String guardianEmail = anomalyMapper.findGuardianEmail(memberId);

        if (guardianEmail == null || guardianEmail.trim().isEmpty()) {
            log.info("[알림 스킵] MemberID: {} - 등록된 보호자 메일이 없거나 수신에 동의하지 않았습니다.", memberId);
            return;
        }

        String memberName = anomalyMapper.findMemberName(memberId);
        if (memberName == null || memberName.trim().isEmpty()) {
            memberName = "회원";
        }

        log.info("[알림 발송] MemberID: {}, 감지항목: {} - 보호자({})에게 이메일을 발송합니다.",
                memberId, metricScope, guardianEmail);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(guardianEmail);
            helper.setSubject("[두뇌산책] 보호자 알림 - 어르신 인지 기능 점검 안내");
            helper.setText(buildAlertHtmlTemplate(memberName, metricScope), true);

            mailSender.send(mimeMessage);
            log.info("이메일 발송 성공!");

        } catch (MessagingException e) {
            log.error("이메일 생성 실패: {}", e.getMessage());
            throw new RuntimeException("이메일 발송 장애 발생");
        } catch (MailException e) {
            log.error("이메일 발송 실패: {}", e.getMessage());
            throw new RuntimeException("이메일 발송 장애 발생");
        }
    }

    private String buildAlertHtmlTemplate(String memberName, MetricScope metricScope) {
        String detectedArea = getDetectedAreaName(metricScope);
        String detectedDescription = getDetectedDescription(metricScope);
        String detectedIcon = getDetectedIcon(metricScope);
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));

        return """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin:0; padding:0; background-color:#F0F4F8; font-family:'Apple SD Gothic Neo','Malgun Gothic','맑은 고딕',sans-serif;">
                    <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#F0F4F8; padding:40px 20px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600" cellpadding="0" cellspacing="0" style="background-color:#FFFFFF; border-radius:16px; overflow:hidden; box-shadow:0 4px 24px rgba(0,0,0,0.08);">
                
                                    <!-- 헤더 -->
                                    <tr>
                                        <td style="background: linear-gradient(135deg, #6366F1 0%%, #8B5CF6 50%%, #A78BFA 100%%); padding:36px 40px; text-align:center;">
                                            <p style="margin:0 0 8px 0; font-size:32px;">🧠</p>
                                            <h1 style="margin:0; color:#FFFFFF; font-size:22px; font-weight:700; letter-spacing:-0.5px;">두뇌산책 보호자 알림</h1>
                                            <p style="margin:8px 0 0 0; color:rgba(255,255,255,0.85); font-size:13px;">%s</p>
                                        </td>
                                    </tr>
                
                                    <!-- 인사말 -->
                                    <tr>
                                        <td style="padding:32px 40px 16px 40px;">
                                            <p style="margin:0; color:#1E293B; font-size:16px; line-height:1.7;">
                                                안녕하세요, <strong>%s</strong>님의 보호자님께 알려드립니다.
                                            </p>
                                        </td>
                                    </tr>
                
                                    <!-- 감지 알림 카드 -->
                                    <tr>
                                        <td style="padding:8px 40px 24px 40px;">
                                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#FEF2F2; border:1px solid #FECACA; border-radius:12px; border-left:4px solid #EF4444;">
                                                <tr>
                                                    <td style="padding:24px;">
                                                        <p style="margin:0 0 4px 0; font-size:20px;">%s</p>
                                                        <p style="margin:0 0 8px 0; color:#DC2626; font-size:15px; font-weight:700;">인지 변화가 감지되었습니다</p>
                                                        <p style="margin:0; color:#7F1D1D; font-size:13px; line-height:1.6;">
                                                            %s
                                                        </p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                
                                    <!-- 상세 정보 -->
                                    <tr>
                                        <td style="padding:0 40px 24px 40px;">
                                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#F8FAFC; border-radius:12px;">
                                                <tr>
                                                    <td style="padding:20px 24px;">
                                                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td style="padding:8px 0; border-bottom:1px solid #E2E8F0;">
                                                                    <span style="color:#64748B; font-size:13px;">감지 영역</span>
                                                                </td>
                                                                <td style="padding:8px 0; border-bottom:1px solid #E2E8F0; text-align:right;">
                                                                    <span style="color:#1E293B; font-size:13px; font-weight:600;">%s</span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding:8px 0; border-bottom:1px solid #E2E8F0;">
                                                                    <span style="color:#64748B; font-size:13px;">감지 일시</span>
                                                                </td>
                                                                <td style="padding:8px 0; border-bottom:1px solid #E2E8F0; text-align:right;">
                                                                    <span style="color:#1E293B; font-size:13px; font-weight:600;">%s</span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding:8px 0;">
                                                                    <span style="color:#64748B; font-size:13px;">대상 회원</span>
                                                                </td>
                                                                <td style="padding:8px 0; text-align:right;">
                                                                    <span style="color:#1E293B; font-size:13px; font-weight:600;">%s님</span>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                
                                    <!-- 안내 메시지 -->
                                    <tr>
                                        <td style="padding:0 40px 24px 40px;">
                                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#EFF6FF; border-radius:12px; border-left:4px solid #3B82F6;">
                                                <tr>
                                                    <td style="padding:20px 24px;">
                                                        <p style="margin:0 0 4px 0; font-size:14px;">💡</p>
                                                        <p style="margin:0; color:#1E40AF; font-size:13px; line-height:1.7; font-weight:600;">보호자님께 드리는 안내</p>
                                                        <p style="margin:8px 0 0 0; color:#1E3A5F; font-size:13px; line-height:1.7;">
                                                            이 알림은 최근 활동 기록을 분석하여 평소보다 유의미한 변화가
                                                            연속으로 감지되었을 때 발송됩니다. 일시적인 컨디션 변화일 수 있으니
                                                            지나치게 걱정하지 마시고, 어르신의 상태를 부드럽게 확인해 주세요.
                                                        </p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                
                                    <!-- 구분선 -->
                                    <tr>
                                        <td style="padding:0 40px;">
                                            <hr style="border:none; border-top:1px solid #E2E8F0; margin:0;">
                                        </td>
                                    </tr>
                
                                    <!-- 푸터 -->
                                    <tr>
                                        <td style="padding:24px 40px 32px 40px; text-align:center;">
                                            <p style="margin:0 0 4px 0; color:#94A3B8; font-size:12px;">본 메일은 두뇌산책 보호자 알림 수신에 동의하신 분께 발송됩니다.</p>
                                            <p style="margin:0; color:#94A3B8; font-size:12px;">수신을 원하지 않으시면 앱 설정에서 변경해 주세요.</p>
                                            <p style="margin:12px 0 0 0; color:#CBD5E1; font-size:11px;">© 두뇌산책 Team</p>
                                        </td>
                                    </tr>
                
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                todayDate,           // 헤더 날짜
                memberName,          // 인사말
                detectedIcon,        // 감지 카드 아이콘
                detectedDescription, // 감지 카드 설명
                detectedArea,        // 상세 - 감지 영역
                todayDate,           // 상세 - 감지 일시
                memberName           // 상세 - 대상 회원
        );
    }

    private String getDetectedAreaName(MetricScope metricScope) {
        return switch (metricScope) {
            case WORD_MEMORY -> "미니게임 — 기억력 (단어 기억)";
            case ARITHMETIC -> "미니게임 — 계산력 (사칙연산)";
            case DESCARTES_RPS -> "미니게임 — 판단력 (데카르트 가위바위보)";
            case QUESTION -> "개방형 질문 — 응답 속도";
        };
    }

    private String getDetectedDescription(MetricScope metricScope) {
        return switch (metricScope) {
            case WORD_MEMORY -> "최근 기억력 게임에서 정답률이 평소보다 눈에 띄게 낮아졌습니다.";
            case ARITHMETIC -> "최근 계산력 게임에서 정답률이 평소보다 눈에 띄게 낮아졌습니다.";
            case DESCARTES_RPS -> "최근 판단력 게임에서 정답률이 평소보다 눈에 띄게 낮아졌습니다.";
            case QUESTION -> "최근 개방형 질문에 대한 응답 시간이 평소보다 눈에 띄게 길어졌습니다.";
        };
    }

    private String getDetectedIcon(MetricScope metricScope) {
        return switch (metricScope) {
            case WORD_MEMORY -> "📝";
            case ARITHMETIC -> "🔢";
            case DESCARTES_RPS -> "✊";
            case QUESTION -> "💬";
        };
    }
}

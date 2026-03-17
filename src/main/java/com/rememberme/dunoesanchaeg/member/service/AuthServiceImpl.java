package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.domain.MemberToken;
import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.member.mapper.MemberTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.rememberme.dunoesanchaeg.member.domain.enums.FontSize.SMALL;
import static com.rememberme.dunoesanchaeg.member.domain.enums.Role.USER;
import static com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus.ACTIVE;
import static com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus.WITHDRAWN;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberMapper memberMapper;
    private final MemberTokenMapper memberTokenMapper;

    @Override
    public KakaoLoginResponse kakaoAuth(String kakaoId, String email, String userAgent) {
        int result;
        Member member = memberMapper.findByKakaoId(kakaoId);

        // JWT 토큰 로직 구현하면 변경해야함-------------------
        String accessToken = java.util.UUID.randomUUID().toString();
        String refreshToken = java.util.UUID.randomUUID().toString();
        LocalDateTime expireDay = LocalDateTime.now().plusDays(14);
        //------------------------------------------------


        // 신규유저면 insertMember 아니면 기존유저
        // 기존 유저에서 getUserStatus 가 WITHDRAWN이면 에러
        // 기존유저이면서 ACTIVE이면 updateLastLoginAt 갱신
        if (member == null) {
            Member newMember = Member.builder()
                    .kakaoId(kakaoId)
                    .email(email)
                    .isHighContrast(false)
                    .fontSize(SMALL)
                    .isProfileCompleted(false)
                    .role(USER)
                    .userStatus(ACTIVE)
                    .build();
            result = memberMapper.insertMember(newMember);
            if (result != 1) {
                throw new BaseException(500, "유저 저장 실패");
            }
            member = newMember;

        } else {
            if (member.getUserStatus() == WITHDRAWN) {
                throw new BaseException(400, "탈퇴한 회원입니다. 30일 이내 복구 가능합니다.");
            }

            result = memberMapper.updateLastLoginAt(member.getMemberId());

            if (result != 1) {
                throw new BaseException(500, "마지막 로그인 갱신 실패");
            }
        }


        // 새 토큰 발행: 로그인이 성공했으므로 새로운 AccessToken과 RefreshToken을 생성
        // 기존 세션 확인: findByMemberIdAndUserAgent로 "이 유저가 이 기기로 들어온 적이 있는지" 확인
        // memberToken == null 이면 새 리프레시토큰과 나머지 설정
        // not null이면 update토큰
        MemberToken memberToken = memberTokenMapper.findByMemberIdAndUserAgent(member.getMemberId(), userAgent);
        if (memberToken == null) {
            MemberToken newMemberToken = MemberToken.builder()
                    .memberId(member.getMemberId())
                    .refreshToken(refreshToken)
                    .userAgent(userAgent)
                    .expiresAt(expireDay)
                    .build();
            result = memberTokenMapper.insertMemberToken(newMemberToken);
            if (result != 1) {
                throw new BaseException(500, "유저 토큰 저장 실패");
            }

            memberToken = newMemberToken;

        } else {
            memberToken.setRefreshToken(refreshToken);
            memberToken.setExpiresAt(expireDay);
            result = memberTokenMapper.updateMemberToken(memberToken);
            if (result != 1) {
                throw new BaseException(500, "유저 토큰 수정 실패");
            }
        }

        return KakaoLoginResponse.builder()
                .memberId(member.getMemberId())
                .isProfileCompleted(member.isProfileCompleted())
                .userStatus(member.getUserStatus())
                .fontSize(member.getFontSize())
                .isHighContrast(member.isHighContrast())
                .accessToken(accessToken)
                .refreshToken(memberToken.getRefreshToken())
                .build();
    }
}
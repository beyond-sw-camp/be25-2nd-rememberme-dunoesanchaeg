package com.rememberme.dunoesanchaeg.service;

import com.rememberme.dunoesanchaeg.domain.Member;
import com.rememberme.dunoesanchaeg.domain.MemberToken;
import com.rememberme.dunoesanchaeg.domain.enums.FontSize;
import com.rememberme.dunoesanchaeg.domain.enums.Role;
import com.rememberme.dunoesanchaeg.domain.enums.UserStatus;
import com.rememberme.dunoesanchaeg.dto.response.KakaoLoginResponse;
import com.rememberme.dunoesanchaeg.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.mapper.MemberTokenMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Builder
@Component
public class AuthServiceImpl implements AuthService{
    private MemberMapper memberMapper;
    private MemberTokenMapper memberTokenMapper;

    @Autowired
    public AuthServiceImpl(MemberMapper memberMapper, MemberTokenMapper memberTokenMapper){
        this.memberMapper = memberMapper;
        this.memberTokenMapper = memberTokenMapper;
    }

    @Override
    public KakaoLoginResponse kakaoAuth(String kakaoId, String email, String userAgent) {
        Member member = memberMapper.findByKakaoId(kakaoId);
        // JWT 토큰 로직 구현하면 변경해야함-------------------
        String accessToken ="mock-access-token-for-jaeha";
        String refreshToken ="mock-refresh-token-for-jaeha";
        LocalDateTime expireDay = LocalDateTime.now().plusDays(14);
        //------------------------------------------------

        // 신규유저면 insertMember 아니면 기존유저
        // 기존 유저에서 getUserStatus 가 WITHDRAWN이면 에러
        // 기존유저이면서 ACTIVE이면 updateLastLoginAt 갱신
        if(member == null){
            Member newMember = Member.builder()
                    .kakaoId(kakaoId)
                    .email(email)
                    .isHighContrast(false)
                    .fontSize(FontSize.SMALL)
                    .isProfileCompleted(false)
                    .role(Role.USER)
                    .userStatus(UserStatus.ACTIVE)
                    .build();
            memberMapper.insertMember(newMember);
            member = newMember;
        } else{
            if(member.getUserStatus() == UserStatus.WITHDRAWN){
                throw new RuntimeException("탈퇴한 회원입니다. 30일 이내 복구 가능합니다.");
            }
            memberMapper.updateLastLoginAt(member.getMemberId());

        }

        // 새 토큰 발행: 로그인이 성공했으므로 새로운 AccessToken과 RefreshToken을 생성
        // 기존 세션 확인: findByMemberIdAndUserAgent로 "이 유저가 이 기기로 들어온 적이 있는지" 확인
        // memberToken == null 이면 새 리프레시토큰과 나머지 설정
        // not null이면 update토큰
        MemberToken memberToken = memberTokenMapper.findByMemberIdAndUserAgent(member.getMemberId(), userAgent);
        if (memberToken == null){
            MemberToken newMemberToken = MemberToken.builder()
                    .memberId(member.getMemberId())
                    .refreshToken(refreshToken)
                    .userAgent(userAgent)
                    .expiresAt(expireDay)
                    .build();
            memberTokenMapper.insertMemberToken(newMemberToken);
            memberToken = newMemberToken;
        }else{
            memberToken.setRefreshToken(refreshToken);
            memberToken.setExpiresAt(expireDay);
            memberTokenMapper.updateMemberToken(memberToken);
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

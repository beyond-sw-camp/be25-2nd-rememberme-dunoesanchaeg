package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.MemberToken;
import com.rememberme.dunoesanchaeg.member.mapper.MemberTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenManager {
    // AuthServiceImpl의 @Transactional으로 인해 throw new BaseException 던지면 서버가 롤백됨
    // 토큰 탈취과정에서 isRevoked를 true로 만들어야 하는데 서버가 롤백되면서 업데이트가 안됨
    // TokenManager에서 새 트랜잭션을 실행시켜서 서버가 업데이트 되도록함

    private final MemberTokenMapper memberTokenMapper;


    // 토큰 폐기
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeToken(MemberToken token) {
        MemberToken revokeToken = token
                .toBuilder()
                .isRevoked(true)
                .build();
        memberTokenMapper.upsertMemberToken(revokeToken);

    }

    //로그아웃
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int logoutTransactional(long memberId, String userAgent) {
        return memberTokenMapper.logoutMemberToken(memberId, userAgent);
    }

    // 전체 로그아웃
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int logoutAllTransactional(long memberId) {
        return memberTokenMapper.logoutAllMemberToken(memberId);
    }

    // 재로그인시 토큰만 업데이트
    // 호출한 쪽의 트랜잭션과 lifecycle이 같음
    @Transactional(propagation = Propagation.REQUIRED)
    public void reactivateToken(MemberToken token, String newRefreshToken,LocalDateTime newExpireDate) {
        MemberToken reactiveToken = token.toBuilder()
                .refreshToken(newRefreshToken)
                .expiresAt(newExpireDate)
                .isRevoked(false).build();
        int result = memberTokenMapper.upsertMemberToken(reactiveToken);

        if(result < 1){
            throw new BaseException(500, "세션 재활성화에 실패했습니다.");
        }

    }
}
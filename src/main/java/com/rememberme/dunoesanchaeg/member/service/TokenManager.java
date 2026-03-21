package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.domain.MemberToken;
import com.rememberme.dunoesanchaeg.member.mapper.MemberTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenManager {
    // AuthServiceImpl의 @Transactional으로 인해 throw new BaseException 던지면 서버가 롤백됨
    // 토큰 탈취과정에서 isRevoked를 true로 만들어야 하는데 서버가 롤백되면서 업데이트가 안됨
    // TokenManager에서 새 트랜잭션을 실행시켜서 서버가 업데이트 되도록함

    private final MemberTokenMapper memberTokenMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 💡 새로운 트랜잭션을 시작!
    public void revokeToken(MemberToken token) {
        token.setRevoked(true);
        memberTokenMapper.updateMemberToken(token);

    }
}
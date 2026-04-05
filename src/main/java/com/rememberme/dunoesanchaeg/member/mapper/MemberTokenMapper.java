package com.rememberme.dunoesanchaeg.member.mapper;

import com.rememberme.dunoesanchaeg.member.domain.MemberToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberTokenMapper {
    // 로그인 한 유저가 같은 기기에서 로그인 한 적있는지 확인
    MemberToken findByMemberIdAndUserAgent(@Param("memberId") Long memberId, @Param("userAgent") String userAgent);

    // 토큰 저장 및 업데이트
    //유효기간이 지난 토큰을 새로 발행한 토큰으로 교체 UPDATE를 통해 userAgent는 그대로인 상태에서 리프레시토큰 수정
    int upsertMemberToken(MemberToken memberToken);

    // 리프레시 토큰이 있는 경우 새 엑세스 토큰만 재발행
    MemberToken findByRefreshToken(String refreshToken);

    // isRevoked를 true로 만들어 토큰 무효화
    int logoutMemberToken(@Param("memberId") Long memberId, @Param("userAgent") String userAgent);

    // 내부 로직용 전체 로그아웃
    int logoutAllMemberToken(Long memberId);

}

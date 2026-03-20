package com.rememberme.dunoesanchaeg.member.mapper;

import com.rememberme.dunoesanchaeg.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    // memberId로 유저 조회
    Member findByMemberId(Long memberId);

    // 카카오아이디로 유저 조회
    Member findByKakaoId(String kakaoId);

    // 데이터가 DB에 잘 들어갔는지 판단.
    int insertMember(Member member);

    // 최종 로그인 시간 갱신
    // return 1이면 성공
    int updateLastLoginAt(Long memberId);

    Member findByEmail(String email);

    int updateEmail(@Param("memberId") Long memberId, @Param("email") String email);
}

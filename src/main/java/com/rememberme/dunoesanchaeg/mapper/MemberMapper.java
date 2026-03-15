package com.rememberme.dunoesanchaeg.mapper;

import com.rememberme.dunoesanchaeg.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    // 카카오아이디로 유저 조회
    Member findByKakaoId(String kakaoId);

    // 데이터가 DB에 잘 들어갔는지 판단.
    int insertMember(Member member);

    // 최종 로그인 시간 갱신
    // return 1이면 성공
    int updateLastLoginAt(Long memberId);

}

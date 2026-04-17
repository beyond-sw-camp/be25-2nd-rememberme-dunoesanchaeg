package com.rememberme.dunoesanchaeg.member.mapper;

import com.rememberme.dunoesanchaeg.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    // memberId로 유저 조회
    Member findByMemberId(@Param("memberId") Long memberId);

    // 카카오아이디로 유저 조회
    Member findByKakaoId(@Param("kakaoId") Long kakaoId);

    // 이메일로 유저 조회
    Member findByEmail(String email);

    // 데이터가 DB에 잘 들어갔는지 판단.
    int insertMember(Member member);

    // 최종 로그인 시간 갱신
    // return 1이면 성공
    int updateLastLoginAt(@Param("memberId") Long memberId);

    // 이메일 갱신
    int updateEmail(@Param("memberId") Long memberId, @Param("email") String email);

    // 전화번호 중복 확인
    boolean findExistMemberPhone(@Param("phone") String phone, @Param("memberId") Long memberId);

    // 멤버 추가 프로필 업데이트
    // 멤버 프로필 수정
    int updateProfile(Member member);

    // 멤버 탈퇴
    int withdrawMember(@Param("memberId") Long memberId);

    // 탈퇴멤버 복구
    int recoveryMember(@Param("memberId") Long memberId);

    // total_routine_count + 1 업데이트
    int incrementTotalRoutineCount(@Param("memberId") Long memberId );

    Member findNameById(Long memberId);
}

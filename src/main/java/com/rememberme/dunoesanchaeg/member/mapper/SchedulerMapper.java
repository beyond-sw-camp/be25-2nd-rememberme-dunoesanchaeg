package com.rememberme.dunoesanchaeg.member.mapper;

import com.rememberme.dunoesanchaeg.member.dto.target.WithdrawalTargetDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchedulerMapper {

    // 만료된 토큰 삭제
    int deleteRevokedToken();

    //탈퇴 회원 조회
    List<WithdrawalTargetDto> selectWithdrawnMember();

    // 카카오 탈퇴 상태 변경
    void updateKakaoUnlinkedStatus(Long memberId);

    // 탈퇴 회원 영구삭제
    int removeMemberPermanently();
}

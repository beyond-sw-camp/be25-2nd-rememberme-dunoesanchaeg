package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.target.WithdrawalTargetDto;
import com.rememberme.dunoesanchaeg.member.mapper.SchedulerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {
    private final SchedulerMapper schedulerMapper;
    private final KakaoClient kakaoClient;

    @Override
    public void removeWithdrawnMembers() {
        List<WithdrawalTargetDto> withdrawalTList = schedulerMapper.selectWithdrawnMember();

        if(!withdrawalTList.isEmpty()){
            for (WithdrawalTargetDto target : withdrawalTList) {
                try {
                    kakaoClient.unlinkKakao(target.getKakaoId());
                    // 정상 성공 시 업데이트
                    schedulerMapper.updateKakaoUnlinkedStatus(target.getMemberId());
                } catch (Exception e) {
                    // 에러 메시지에 -101(NotRegisteredUserException)이 포함
                    if (e.getMessage().contains("-101")) {
                        log.info("이미 카카오 연동이 해제된 유저입니다. DB 상태를 업데이트합니다. kakaoId: {}", target.getKakaoId());
                        // 이미 끊긴 것이 확인되었으므로 DB를 1로 바꿈
                        schedulerMapper.updateKakaoUnlinkedStatus(target.getMemberId());
                    } else {
                        log.error("회원 탈퇴 처리 중 진짜 에러 발생 (memberId: {}): {}",
                                target.getMemberId(), e.getMessage());
                    }
                }
            }
        }else {
            log.info("새로 연동 해제할 대상자가 없습니다. 기존 처리 건 삭제를 진행합니다.");
        }

        // 최종적으로 연동 해제가 확인된 유저들만 영구 삭제
        int result = schedulerMapper.removeMemberPermanently();
        log.info("탈퇴 처리 완료 - 총 {}건 영구 삭제", result);
    }

}

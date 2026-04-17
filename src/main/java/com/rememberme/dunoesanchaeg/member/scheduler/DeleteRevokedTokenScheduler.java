package com.rememberme.dunoesanchaeg.member.scheduler;

import com.rememberme.dunoesanchaeg.member.mapper.SchedulerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeleteRevokedTokenScheduler {
    private final SchedulerMapper schedulerMapper;

    // 매 새벽 3시마다 실행
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteRevokedToken(){
        try {
            log.info("Delete Revoked Token 시작");
            int result = schedulerMapper.deleteRevokedToken();
            log.info("토큰스케줄러 작동 result: {}", result);
            log.info("Delete Revoked Token 종료");
        }catch (Exception e){
            log.error("토큰스케줄러 실패: {}", e.getMessage());
        }
    }
}

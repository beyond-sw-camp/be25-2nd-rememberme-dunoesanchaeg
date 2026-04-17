package com.rememberme.dunoesanchaeg.member.scheduler;

import com.rememberme.dunoesanchaeg.member.service.WithdrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawalScheduler {
    private final WithdrawalService withdrawalService;

    // 매일 새벽 3시에 실행 (초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 3 * * *")
    public void runWithdrawal() {
        withdrawalService.removeWithdrawnMembers();
    }
}

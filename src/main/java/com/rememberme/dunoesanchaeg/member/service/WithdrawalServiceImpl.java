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

    @Override
    public void removeWithdrawnMembers() {
        List<WithdrawalTargetDto> withdrawalTList = schedulerMapper.selectWithdrawnMember();
        
        //TODO 여기서부터 작업하면 됨

    }
}

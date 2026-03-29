package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.memory.domain.DailyQuestionLog;
import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;
import com.rememberme.dunoesanchaeg.memory.mapper.DailyQuestionLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyQuestionLogServiceImpl implements DailyQuestionLogService {

    private final DailyQuestionLogMapper dailyQuestionLogMapper;

    @Override
    public boolean existsTodayQuestionLog(Long memberId, LocalDate recordDate, DailyQuestionLogStatus dailyQuestionLogStatus) {

        return dailyQuestionLogMapper.countTodayQuestionLog(memberId, recordDate, dailyQuestionLogStatus) > 0;
    }

    @Override
    public Long createTodayQuestionLog(DailyQuestionLog dailyQuestionLog) {
        int result;

        result = dailyQuestionLogMapper.insertTodayQuestionLog(dailyQuestionLog);
        if (result != 1) {

            throw new BaseException(500, "참여 기록을 저장하지 못하였습니다.");
        }

        return dailyQuestionLog.getDailyQuestionLogId();
    }

    @Override
    public void delete(Long memberId, Long dailyQuestionLogId) {
        int result;

        result = dailyQuestionLogMapper.deleteTodayQuestionLog(memberId, dailyQuestionLogId);
        if (result != 1) {

            throw new BaseException(500, "참여 기록을 삭제하지 못하였습니다.");
        }
    }

    @Override
    public void updateTodayQuestionLog(DailyQuestionLog dailyQuestionLog) {
        int result;

        result = dailyQuestionLogMapper.updateTodayQuestionLog(dailyQuestionLog);
        if (result != 1) {

            throw new BaseException(500, "참여 기록을 저장하지 못하였습니다.");
        }
    }
}
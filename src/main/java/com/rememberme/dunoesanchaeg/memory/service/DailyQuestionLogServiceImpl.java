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
    public boolean existsTodayQuestionLog(Long memberId, LocalDate recordDate, DailyQuestionLogStatus status) {

        return dailyQuestionLogMapper.countTodayQuestionLog(memberId, recordDate, status) > 0;
    }

    // 추가
    @Override
    public DailyQuestionLog getTodayQuestionLog(Long memberId, LocalDate recordDate) {

        return dailyQuestionLogMapper.selectTodayQuestionLog(memberId, recordDate);
    }

    @Override
    public Long createTodayQuestionLog(DailyQuestionLog dailyQuestionLog) {
        int result;

        result = dailyQuestionLogMapper.insertTodayQuestionLog(dailyQuestionLog);
        if (result != 1) {

            throw new BaseException(500, "개방형질문 루틴 시작 처리에 실패하였습니다.");
        }

        if (dailyQuestionLog.getDailyQuestionLogId() == null) {

            throw new BaseException(500, "개방형질문 내용 조회에 실패하였습니다.");
        }

        return dailyQuestionLog.getDailyQuestionLogId();
    }

    @Override
    public void delete(Long memberId, Long dailyQuestionLogId) {
        int result;

        result = dailyQuestionLogMapper.deleteTodayQuestionLog(memberId, dailyQuestionLogId);
        if (result != 1) {

            throw new BaseException(500, "개방형질문 루틴 이탈 처리에 실패하였습니다.");
        }
    }

    @Override
    public void updateTodayQuestionLog(DailyQuestionLog dailyQuestionLog) {
        int result;

        result = dailyQuestionLogMapper.updateTodayQuestionLog(dailyQuestionLog);
        if (result != 1) {

            throw new BaseException(500, "개방형질문 루틴 완료 처리에 실패하였습니다.");
        }
    }
}
package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.memory.domain.DailyQuestionLog;
import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;

import java.time.LocalDate;

// 개방형질문 로그 관리 담당
public interface DailyQuestionLogService {

    boolean existsTodayQuestionLog(Long memberId, LocalDate recordDate, DailyQuestionLogStatus dailyQuestionLogStatus);

    Long createTodayQuestionLog(DailyQuestionLog dailyQuestionLog);

    void delete(Long memberId, Long dailyQuestionLogId);

    void updateTodayQuestionLog(DailyQuestionLog dailyQuestionLog);
}

package com.rememberme.dunoesanchaeg.memory.mapper;

import com.rememberme.dunoesanchaeg.memory.domain.DailyQuestionLog;
import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface DailyQuestionLogMapper {
    // 개방형질문 로그 확인
    int countTodayQuestionLog(@Param("memberId") Long memberId, @Param("recordDate") LocalDate recordDate, @Param("status") DailyQuestionLogStatus status);

    // 개방형질문 status=STARTED 로그 조회
    DailyQuestionLog selectTodayQuestionLog(@Param("memberId") Long memberId, @Param("recordDate") LocalDate recordDate);

    // 개방형질문 로그 저장
    int insertTodayQuestionLog(DailyQuestionLog dailyQuestionLog);

    // 개방형질문 로그 삭제
    int deleteTodayQuestionLog(@Param("memberId") Long memberId, @Param("dailyQuestionLogId") Long dailyQuestionLogId);

    // 개방형질문 로그 업데이트
    int updateTodayQuestionLog(DailyQuestionLog dailyQuestionLog);
}

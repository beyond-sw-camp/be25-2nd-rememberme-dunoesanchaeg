package com.rememberme.dunoesanchaeg.routines.mapper;

import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface RoutineMapper {

    // 오늘의 루틴 조회 (memberId와 오늘 날짜로)
    DailyRoutineStatus findByMemberIdAndDate(@Param("memberId") Long memberId,
                                             @Param("routineDate") LocalDate routineDate);

    // 오늘의 루틴 삽입
    int insertTodayRoutine(DailyRoutineStatus routine);

    // 루틴 아이디로 오늘의 루틴 조회
    DailyRoutineStatus findByRoutineId(@Param("routineId") Long routineId);

    // 오늘의 루틴 게임 완료 업데이트
    int updateGameComplete(@Param("routineId") Long routineId);

    // 오늘의 루틴 기록 완료 업데이트
    int updateRecordComplete(@Param("routineId")Long routineId);

    // 오늘의 루틴 질문 완료 업데이트
    int updateQuestionComplete(@Param("routineId")Long routineId);

    // 개방형질문 기능 구현에 필요한 코드
    Long selectAssignedQuestionId(@Param("memberId") Long memberId, @Param("routineDate") LocalDate routineDate);
}
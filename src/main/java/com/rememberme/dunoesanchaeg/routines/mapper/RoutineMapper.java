package com.rememberme.dunoesanchaeg.routines.mapper;

import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface RoutineMapper {

    // 오늘의 루틴 조회 (memberId와 오늘 날짜로)
    DailyRoutineStatus findByMemberIdAndDate(@Param("memberId") Long memberId,
                                             @Param("createdAt") LocalDateTime createdAt);

    // 오늘의 루틴 삽입
    void insertTodayRoutine(DailyRoutineStatus routine);

    // 오늘의 루틴 업데이트
    void updateTodayRoutine(DailyRoutineStatus routine);
}
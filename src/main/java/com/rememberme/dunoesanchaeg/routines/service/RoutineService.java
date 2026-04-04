package com.rememberme.dunoesanchaeg.routines.service;

import com.rememberme.dunoesanchaeg.routines.dto.response.RoutineResponse;

import java.time.LocalDate;

public interface RoutineService {

    RoutineResponse getTodayRoutine(Long memberId);

    // 개방형질문 기능 구현에 필요한 코드
    Long getAssignedQuestionId(Long memberId, LocalDate routineDate);
}

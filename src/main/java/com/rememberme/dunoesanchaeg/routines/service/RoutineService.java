package com.rememberme.dunoesanchaeg.routines.service;

import com.rememberme.dunoesanchaeg.routines.domain.enums.MissionTypes;
import com.rememberme.dunoesanchaeg.routines.dto.response.RoutineResponse;

import java.time.LocalDate;

public interface RoutineService {

    // 오늘의 루틴 조회
    RoutineResponse getTodayRoutine(Long memberId);

    // 개방형질문 기능 구현에 필요한 코드
    Long getAssignedQuestionId(Long memberId, LocalDate routineDate);

    // GAME, RECORD, QUESTION 완료 업데이트
    void completeRoutineItem(Long memberId, MissionTypes missionTypes);

    // totalRoutineCount 계산 (트로피 연결)
    void totalRoutineCount(Long memberId);
}
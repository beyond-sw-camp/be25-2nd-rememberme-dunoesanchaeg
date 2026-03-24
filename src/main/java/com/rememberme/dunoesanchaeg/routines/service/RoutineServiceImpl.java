package com.rememberme.dunoesanchaeg.routines.service;

import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import com.rememberme.dunoesanchaeg.routines.dto.response.RoutineResponse;
import com.rememberme.dunoesanchaeg.routines.mapper.RoutineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineMapper routineMapper;

    @Override
    public RoutineResponse getTodayRoutine(Long memberId) {

        LocalDate today = LocalDate.now();

        // 1. 오늘의 루틴 조회
        DailyRoutineStatus routine = routineMapper.findByMemberIdAndDate(memberId, today);

        // 2. 없으면 생성 필요
        if (routine == null) {
            routine = createRoutine(memberId, today);
            routineMapper.insertTodayRoutine(routine);
        }

        // 3. progressRate 구현
        int completedCount = 0;

        if (routine.getIsGameFinished()) completedCount++;
        if (routine.getIsRecordFinished()) completedCount++;
        if (routine.getIsQuestionFinished()) completedCount++;

        int progressRate = (int) ((completedCount / 3.0) * 100);

        // completedCount에 따른 feedbackMessage
        String feedbackMsg = "";

        if (completedCount == 0) {
            feedbackMsg = "아직 루틴을 시작하지 않았어요. 함께 시작해볼까요?";
        } else if (completedCount == 1) {
            feedbackMsg = "조금만 더 힘내세요! 5분이면 충분합니다.";
        } else if (completedCount == 2) {
            feedbackMsg = "거의 다 왔어요! 하나만 더 하면 완벽해요!";
        } else if (completedCount == 3){
            feedbackMsg = "오늘의 루틴을 모두 완료했어요. 수고했어요!";
        }

        // 4. Dto 변환
        return RoutineResponse.builder()
                .routineId(routine.getRoutineId())
                .routineDate(routine.getRoutineDate())
                .assignedGameType(routine.getAssignedGameType().name())
                .assignedQuestionId(routine.getAssignedQuestionId())
                .isGameFinished(routine.getIsGameFinished())
                .isRecordFinished(routine.getIsRecordFinished())
                .isQuestionFinished(routine.getIsQuestionFinished())
                .completedCnt(completedCount)
                .progressRate(progressRate)
                .feedbackMsg(feedbackMsg)
                .build();
    }

    // 루틴 생성
    private DailyRoutineStatus createRoutine(Long memberId, LocalDate today) {
        return DailyRoutineStatus.builder()
                .memberId(memberId)
                .routineDate(LocalDate.now())
                .assignedGameType(randomGame())
                .assignedQuestionId(randomQuestion())
                .isGameFinished(false)
                .isRecordFinished(false)
                .isQuestionFinished(false)
                .build();
    }

    // 게임 랜덤 배정
    private AssignedGameType randomGame() {
        AssignedGameType[] games = AssignedGameType.values();
        return games[new Random().nextInt(games.length)];
    }

    // 개방형 질문 랜덤 배정
    private Long randomQuestion() {
        return (long) (new Random().nextInt(10) + 1);
    }
}

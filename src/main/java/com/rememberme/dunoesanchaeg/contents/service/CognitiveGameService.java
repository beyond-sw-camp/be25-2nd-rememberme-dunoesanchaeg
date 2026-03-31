package com.rememberme.dunoesanchaeg.contents.service;

import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;
import com.rememberme.dunoesanchaeg.contents.mapper.CognitiveGameMapper;
import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import com.rememberme.dunoesanchaeg.routines.mapper.RoutineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CognitiveGameServiceImpl implements CognitiveGameService {

    private final RoutineMapper routineMapper;
    private final CognitiveGameMapper cognitiveGameMapper;

    @Override
    public TodayGameResponse getTodayGame(Long memberId) {

        DailyRoutineStatus routine =
                routineMapper.findByMemberIdAndDate(memberId, LocalDate.now());

        if (routine == null) {
            throw new IllegalArgumentException("오늘 게임 없음");
        }

        return TodayGameResponse.builder()
                .gameType(routine.getAssignedGameType())
                .isGameFinished(routine.getIsGameFinished())
                .build();
    }

    @Override
    public void saveGameResult(Long memberId, GameResultSaveRequest request) {

        cognitiveGameMapper.insertGameLog(
                memberId,
                request.getGameType(),
                request.getCorrectCount(),
                request.getTotalTryCount()
        );

        // 루틴 상태 업데이트
        routineMapper.updateGameFinished(memberId, LocalDate.now());
    }
}
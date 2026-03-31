package com.rememberme.dunoesanchaeg.contents.service;

import com.rememberme.dunoesanchaeg.contents.dto.request.AnswerSubmitRequest;
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

    private final CognitiveGameMapper cognitiveGameMapper;
    private final RoutineMapper routineMapper;

    @Override
    public TodayGameResponse getTodayGame(Long memberId) {
        DailyRoutineStatus routine =
                routineMapper.findByMemberIdAndDate(memberId, LocalDate.now());

        if (routine == null) {
            throw new IllegalArgumentException("오늘의 미니게임 정보를 찾을 수 없습니다.");
        }

        return TodayGameResponse.builder()
                .gameType(routine.getAssignedGameType())
                .isGameFinished(routine.getIsGameFinished())
                .build();
    }

    @Override
    public void saveGameResult(Long memberId, AnswerSubmitRequest request) {
        cognitiveGameMapper.insertGameResult(
                memberId,
                request.getGameType(),
                request.getCorrectCount(),
                request.getTotalTryCount()
        );

        // routineMapper.updateGameFinished(memberId, LocalDate.now());
    }
}
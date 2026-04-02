package com.rememberme.dunoesanchaeg.contents.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.contents.dto.request.AnswerSubmitRequest;
import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;
import com.rememberme.dunoesanchaeg.contents.mapper.CognitiveGameMapper;
import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import com.rememberme.dunoesanchaeg.routines.mapper.RoutineMapper;
import com.rememberme.dunoesanchaeg.routines.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CognitiveGameServiceImpl implements CognitiveGameService {

    private final CognitiveGameMapper cognitiveGameMapper;
    private final RoutineMapper routineMapper;
    private final RoutineService routineService;

    @Override
    public TodayGameResponse getTodayGame(Long memberId) {
        DailyRoutineStatus routine =
                routineMapper.findByMemberIdAndDate(memberId, LocalDate.now());

        if (routine == null) {
            throw new BaseException(404, "오늘의 미니게임 정보를 찾을 수 없습니다.");
        }

        return TodayGameResponse.builder()
                .playedDate(routine.getRoutineDate())
                .gameType(routine.getAssignedGameType())
                .totalRounds(3)
                .roundTimeLimitSec(15)
                .passCondition("AT_LEAST_ONE_CORRECT")
                .isGameFinished(routine.getIsGameFinished())
                .build();
    }

    @Override
    public void saveGameResult(Long memberId, AnswerSubmitRequest request) {
        DailyRoutineStatus routine =
                routineMapper.findByMemberIdAndDate(memberId, LocalDate.now());

        if (routine == null) {
            throw new BaseException(404, "오늘의 미니게임 정보를 찾을 수 없습니다.");
        }

        if (Boolean.TRUE.equals(routine.getIsGameFinished())) {
            throw new BaseException(409, "이미 완료된 미니게임입니다. 홈 화면으로 이동해주세요.");
        }

        if (routine.getAssignedGameType() != request.getGameType()) {
            throw new BaseException(409, "배정된 게임과 요청한 게임 타입이 일치하지 않습니다. 홈 화면으로 이동해주세요.");
        }

        validateGameResult(request);

        cognitiveGameMapper.insertGameResult(
                memberId,
                LocalDate.now(),
                request.getGameType(),
                request.getCorrectCount(),
                request.getTotalTryCount()
        );

        routineMapper.updateGameComplete(routine.getRoutineId());

        routineService.completeRoutineItem(memberId, "GAME");
    }

    private void validateGameResult(AnswerSubmitRequest request) {
        if (request.getCorrectCount() == null || request.getCorrectCount() < 0) {
            throw new BaseException(400, "유효하지 않은 게임 결과입니다. 홈 화면으로 이동해주세요.");
        }

        if (request.getTotalTryCount() == null || request.getTotalTryCount() <= 0) {
            throw new BaseException(400, "유효하지 않은 게임 결과입니다. 홈 화면으로 이동해주세요.");
        }

        if (request.getCorrectCount() > request.getTotalTryCount()) {
            throw new BaseException(400, "정답 개수는 총 시도 횟수를 초과할 수 없습니다. 홈 화면으로 이동해주세요.");
        }
    }
}
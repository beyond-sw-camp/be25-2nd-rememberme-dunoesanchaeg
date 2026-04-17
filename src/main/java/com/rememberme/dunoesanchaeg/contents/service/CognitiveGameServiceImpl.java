package com.rememberme.dunoesanchaeg.contents.service;

import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import com.rememberme.dunoesanchaeg.analysis.domain.event.CognitiveEvent;
import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.contents.dto.request.AnswerSubmitRequest;
import com.rememberme.dunoesanchaeg.contents.dto.request.GameResultInsertDto;
import com.rememberme.dunoesanchaeg.contents.dto.response.GameFinishedResponse;
import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;
import com.rememberme.dunoesanchaeg.contents.mapper.CognitiveGameMapper;
import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import com.rememberme.dunoesanchaeg.routines.domain.enums.MissionTypes;
import com.rememberme.dunoesanchaeg.routines.mapper.RoutineMapper;
import com.rememberme.dunoesanchaeg.routines.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CognitiveGameServiceImpl implements CognitiveGameService {

    private final CognitiveGameMapper cognitiveGameMapper;
    private final RoutineMapper routineMapper;
    private final RoutineService routineService;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int TOTAL_ROUNDS = 3;
    private static final int ROUND_TIME_LIMIT_SEC = 15;
    private static final String PASS_CONDITION = "ROUND_ENDS_ON_CORRECT_OR_TIMEOUT";

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
                .totalRounds(TOTAL_ROUNDS)
                .roundTimeLimitSec(ROUND_TIME_LIMIT_SEC)
                .passCondition(PASS_CONDITION)
                .isGameFinished(routine.getIsGameFinished())
                .build();
    }

    @Override
    public GameFinishedResponse saveGameResult(Long memberId, AnswerSubmitRequest request) {
        LocalDate today = LocalDate.now();

        DailyRoutineStatus routine =
                routineMapper.findByMemberIdAndDate(memberId, today);

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

        boolean isValid = true;

        GameResultInsertDto dto = GameResultInsertDto.builder()
                .memberId(memberId)
                .playedDate(today)
                .gameType(request.getGameType())
                .correctCount(request.getCorrectCount())
                .wrongCount(request.getWrongCount())
                .timeoutCount(request.getTimeoutCount())
                .totalTryCount(request.getTotalTryCount())
                .totalPlayedTime(request.getTotalPlayedTime())
                .isValid(isValid)
                .build();

        int inserted = cognitiveGameMapper.insertGameResult(dto);

        if (inserted != 1) {
            throw new BaseException(500, "게임 결과 저장에 실패했습니다.");
        }

        routineMapper.updateGameComplete(routine.getRoutineId());
        routineService.completeRoutineItem(memberId, MissionTypes.GAME);

        applicationEventPublisher.publishEvent(
                new CognitiveEvent(this, memberId, MetricScope.valueOf(request.getGameType().name()))
        );

        return GameFinishedResponse.builder()
                .correctCount(request.getCorrectCount())
                .wrongCount(request.getWrongCount())
                .timeoutCount(request.getTimeoutCount())
                .totalTryCount(request.getTotalTryCount())
                .totalPlayedTime(request.getTotalPlayedTime())
                .totalRounds(TOTAL_ROUNDS)
                .isGameFinished(true)
                .isValid(isValid)
                .build();
    }

    private void validateGameResult(AnswerSubmitRequest request) {
        if (request.getCorrectCount() < 0 ||
                request.getWrongCount() < 0 ||
                request.getTimeoutCount() < 0 ||
                request.getTotalTryCount() < 0 ||
                request.getTotalPlayedTime() < 0) {
            throw new BaseException(400, "게임 결과 값은 0 이상이어야 합니다.");
        }

        if (request.getTotalTryCount() < 1) {
            throw new BaseException(400, "총 시도 횟수는 최소 1회 이상이어야 합니다.");
        }

        if (request.getCorrectCount() > TOTAL_ROUNDS ||
                request.getTimeoutCount() > TOTAL_ROUNDS) {
            throw new BaseException(400, "정답 개수 또는 시간 초과 횟수가 총 판 수를 초과할 수 없습니다.");
        }

        if (request.getCorrectCount() + request.getTimeoutCount() != TOTAL_ROUNDS) {
            throw new BaseException(400, "정답 개수와 시간 초과 횟수의 합은 3이어야 합니다.");
        }

    }
}
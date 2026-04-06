package com.rememberme.dunoesanchaeg.contents.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.contents.dto.request.AnswerSubmitRequest;
import com.rememberme.dunoesanchaeg.contents.dto.request.GameResultInsertDto;
import com.rememberme.dunoesanchaeg.contents.dto.response.GameFinishedResponse;
import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;
import com.rememberme.dunoesanchaeg.contents.mapper.CognitiveGameMapper;
import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import com.rememberme.dunoesanchaeg.routines.mapper.RoutineMapper;
import com.rememberme.dunoesanchaeg.routines.service.RoutineService;
import lombok.RequiredArgsConstructor;
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

    private static final int TOTAL_ROUNDS = 3;
    private static final int ROUND_TIME_LIMIT_SEC = 15;
    private static final int MIN_VALID_PLAY_TIME = 10;
    private static final String PASS_CONDITION = "AT_LEAST_ONE_CORRECT";

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

        boolean isValid = request.getPlayTimeSeconds() >= MIN_VALID_PLAY_TIME
                && request.getCorrectCount() <= request.getTotalTryCount();

        GameResultInsertDto dto = GameResultInsertDto.builder()
                .memberId(memberId)
                .playedDate(today)
                .gameType(request.getGameType())
                .correctCount(request.getCorrectCount())
                .totalTryCount(request.getTotalTryCount())
                .playTimeSeconds(request.getPlayTimeSeconds())
                .isValid(isValid)
                .build();

        int inserted = cognitiveGameMapper.insertGameResult(dto);

        if(inserted != 1){
            throw new BaseException(500, "게임 결과 저장에 실패했습니다.");
        }

        routineMapper.updateGameComplete(routine.getRoutineId());

        // 희주님 루틴 업데이트 구현되면 넣기
        // routineService.completeRoutineItem(memberId, "GAME");

        return GameFinishedResponse.builder()
                .correctCount(request.getCorrectCount())
                .totalRounds(TOTAL_ROUNDS)
                .isGameFinished(true)
                .isValid(isValid)
                .build();
    }

    private void validateGameResult(AnswerSubmitRequest request) {

        if (request.getCorrectCount() > request.getTotalTryCount()) {
            throw new BaseException(400, "정답 개수는 총 시도 횟수를 초과할 수 없습니다. 홈 화면으로 이동해주세요.");
        }
    }
}
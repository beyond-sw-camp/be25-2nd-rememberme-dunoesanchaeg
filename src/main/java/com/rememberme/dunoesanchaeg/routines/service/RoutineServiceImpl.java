package com.rememberme.dunoesanchaeg.routines.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.routines.domain.DailyRoutineStatus;
import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import com.rememberme.dunoesanchaeg.routines.domain.enums.MissionTypes;
import com.rememberme.dunoesanchaeg.routines.dto.response.RoutineResponse;
import com.rememberme.dunoesanchaeg.routines.mapper.RoutineMapper;
import com.rememberme.dunoesanchaeg.trophies.service.TrophyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineMapper routineMapper;
    private final MemberMapper memberMapper;
    private final TrophyService trophyService;
    private final Random random = new Random();

    // 오늘의 루틴 조회
    @Override
    @Transactional
    public RoutineResponse getTodayRoutine(Long memberId) {

        // 멤버 아이디가 존재하지 않는 경우
        if (memberId == null) {
            throw new BaseException(401, "인증 정보가 유효하지 않습니다.");
        }

        LocalDate today = LocalDate.now();
        DailyRoutineStatus routine = routineMapper.findByMemberIdAndDate(memberId, today);

        // 오늘의 루틴이 없는 경우 생성
        if (routine == null) {
            routine = handleConcurrentCreation(memberId, today);
        }

        return buildResponse(routine);
    }

    // 루틴 생성 시 발생 가능한 중복 생성 방지
    private DailyRoutineStatus handleConcurrentCreation(Long memberId, LocalDate today) {
        // 루틴 생성
        DailyRoutineStatus newRoutine = createRoutine(memberId, today);

        try {
            int result = routineMapper.insertTodayRoutine(newRoutine);
            if (result != 1) {
                throw new BaseException(500, "오늘의 루틴 생성 중 DB INSERT가 실패했습니다.");
            }
            return newRoutine;
        } catch (DuplicateKeyException e) {
            // 거의 동시에 요청이 들어와 이미 생성된 경우, 재조회하여 반환
            DailyRoutineStatus existing = routineMapper.findByMemberIdAndDate(memberId, today);
            if (existing == null) {
                throw new BaseException(500, "루틴 생성 중 충돌이 발생했습니다.");
            }
            return existing;
        } catch (Exception e) {
            log.error("루틴 생성 중 서버 내부 오류 발생: {}", e.getMessage());
            throw new BaseException(500, "루틴 생성 중 서버 내부 오류가 발생했습니다.");
        }
    }

    // GAME, RECORD, QUESTION 완료로 업데이트
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void completeRoutineItem(Long memberId, MissionTypes missionTypes) {
        // 타입 검증
        if (missionTypes == null) {
            throw new BaseException(400, "루틴 타입이 필요합니다.");
        }

        LocalDate today = LocalDate.now();
        DailyRoutineStatus routine = routineMapper.findByMemberIdAndDate(memberId, today);

        if (routine == null) {
            throw new BaseException(404, "오늘의 루틴 정보를 찾을 수 없습니다.");
        }

        // 1. GAME, RECORD, QUESTION -> TRUE로 업데이트
        int updateResult = processUpdate(routine, missionTypes);

        // 2. 항목이 처음으로 완료 처리된 경우에만 전체 완료 체크 진행
        if (updateResult == 1) {
            // 업데이트된 최신 루틴 상태 조회
            DailyRoutineStatus updatedRoutine = routineMapper.findByRoutineId(routine.getRoutineId());

            // 3. 게임, 기록, 질문이 모두 TRUE인지 확인
            if (isAllFinished(updatedRoutine)) {
                this.totalRoutineCount(memberId);
            }
        }
    }

    // GAME, RECORD, QUESTION -> TRUE로 업데이트
    private int processUpdate(DailyRoutineStatus routine, MissionTypes missionTypes) {
        return switch (missionTypes) {
            case GAME -> Boolean.FALSE.equals(routine.getIsGameFinished()) ?
                    routineMapper.updateGameComplete(routine.getRoutineId()) : 0;
            case RECORD -> Boolean.FALSE.equals(routine.getIsRecordFinished()) ?
                    routineMapper.updateRecordComplete(routine.getRoutineId()) : 0;
            case QUESTION -> Boolean.FALSE.equals(routine.getIsQuestionFinished()) ?
                    routineMapper.updateQuestionComplete(routine.getRoutineId()) : 0;
//            default -> throw new BaseException(400, "잘못된 루틴 타입입니다: " + missionTypes);
        };
    }

    // 트로피를 위한 total_routine_count 계산
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void totalRoutineCount(Long memberId) {
        // Member 테이블의 total_routine_count를 1 증가시킴
        int affectedRows = memberMapper.incrementTotalRoutineCount(memberId);

        // 트로피 로직에서 예외가 발생해도 루틴 완료는 유지되도록 예외 처리 고려 가능
        if (affectedRows == 1) {
            try {
                trophyService.awardRoutineCountTrophy(memberId);
            } catch (Exception e) {
                log.error("트로피 수여 중 오류 발생: {}", e.getMessage());
            }
        }
    }

    // 미션이 모두 끝났는지 확인
    private boolean isAllFinished(DailyRoutineStatus updatedRoutine) {
        return Boolean.TRUE.equals(updatedRoutine.getIsGameFinished()) &&
                Boolean.TRUE.equals(updatedRoutine.getIsRecordFinished()) &&
                Boolean.TRUE.equals(updatedRoutine.getIsQuestionFinished());
    }

    // 조회 시 리턴할 응답
    private RoutineResponse buildResponse(DailyRoutineStatus routine) {

        int completedCount = 0;

        if (Boolean.TRUE.equals(routine.getIsGameFinished())) completedCount++;
        if (Boolean.TRUE.equals(routine.getIsRecordFinished())) completedCount++;
        if (Boolean.TRUE.equals(routine.getIsQuestionFinished())) completedCount++;

        int progressRate = (int) ((completedCount / 3.0) * 100);

        String feedbackMsg = switch (completedCount) {
            case 0 -> "아직 루틴을 시작하지 않았어요. 함께 시작해볼까요?";
            case 1 -> "조금만 더 힘내세요! 5분이면 충분합니다.";
            case 2 -> "거의 다 왔어요! 하나만 더 하면 완벽해요!";
            case 3 -> "오늘의 루틴을 모두 완료했어요. 수고했어요!";
            default -> "";
        };

        Member member = memberMapper.findByMemberId(routine.getMemberId());
        String name = (member != null) ? member.getName() : "회원";

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
                .createdAt(routine.getCreatedAt())
                .username(name)
                .build();
    }

    // 오늘의 루틴 조회 시 없다면 오늘의 루틴 생성
    private DailyRoutineStatus createRoutine(Long memberId, LocalDate today) {
        return DailyRoutineStatus.builder()
                .memberId(memberId)
                .routineDate(today)
                .assignedGameType(randomGame())
                .assignedQuestionId(randomQuestion())
                .isGameFinished(false)
                .isRecordFinished(false)
                .isQuestionFinished(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 랜덤 게임 부여
    private AssignedGameType randomGame() {
        AssignedGameType[] games = AssignedGameType.values();
        return games[random.nextInt(games.length)];
    }

    // 랜덤 질문 부여
    private Long randomQuestion() {
        return (long) (random.nextInt(61) + 1);
    }

    // 개방형질문 기능 구현에 필요한 코드
    @Override
    public Long getAssignedQuestionId(Long memberId, LocalDate routineDate) {

        return routineMapper.selectAssignedQuestionId(memberId, routineDate);
    }
}
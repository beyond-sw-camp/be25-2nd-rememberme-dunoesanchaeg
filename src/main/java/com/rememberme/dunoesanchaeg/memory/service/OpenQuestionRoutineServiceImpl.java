package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.memory.domain.DailyQuestionLog;
import com.rememberme.dunoesanchaeg.memory.domain.QuestionBank;
import com.rememberme.dunoesanchaeg.memory.domain.enums.DailyQuestionLogStatus;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionCompleteResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionExitResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionStartResponse;
import com.rememberme.dunoesanchaeg.routines.domain.enums.MissionTypes;
import com.rememberme.dunoesanchaeg.routines.service.RoutineService;
import com.rememberme.dunoesanchaeg.analysis.domain.event.CognitiveEvent;
import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OpenQuestionRoutineServiceImpl implements OpenQuestionRoutineService {

    private final QuestionBankService questionBankService;
    private final DailyQuestionLogService dailyQuestionLogService;

    // 일일 루틴 생성 서비스
    private final RoutineService routineService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public OpenQuestionStartResponse startOpenQuestion(Long memberId) {

        if (memberId == null) {

            throw new BaseException(400, "잘못된 요청입니다.");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        if (dailyQuestionLogService.existsTodayQuestionLog(memberId, today, DailyQuestionLogStatus.COMPLETED)) {

            throw new BaseException(409, "오늘은 이미 참여하였습니다.");
        } else if (dailyQuestionLogService.existsTodayQuestionLog(memberId, today, DailyQuestionLogStatus.EXITED)) {

            throw new BaseException(409, "오늘은 다시 참여할 수 없습니다.");
        }

        // 일일 루틴에서 배정된 질문 ID 조회
        Long assignedQuestionId = routineService.getAssignedQuestionId(memberId, today);

        if (assignedQuestionId == null) {

            throw new BaseException(500, "현재 요청을 처리할 수 없습니다. 고객센터(☎010-1234-1234)로 문의 주시기 바랍니다.");
        }

        // 해당 질문 정보 조회
        QuestionBank questionBank = questionBankService.getOpenQuestionById(assignedQuestionId);

        if (questionBank == null || !questionBank.getIsActive()) {

            throw new BaseException(404, "현재 질문을 확인할 수 없습니다. 고객센터(☎010-1234-1234)로 문의 주시기 바랍니다.");
        }

        // 활동 로그 생성
        DailyQuestionLog dailyQuestionLog = DailyQuestionLog.builder()
                .memberId(memberId)
                .recordDate(today)
                .questionId(questionBank.getQuestionId())
                .status(DailyQuestionLogStatus.STARTED)
                .createdAt(now)
                .build();

        // 추가
        try {
            Long dailyQuestionLogId = dailyQuestionLogService.createTodayQuestionLog(dailyQuestionLog);
            dailyQuestionLog.setDailyQuestionLogId(dailyQuestionLogId);
        } catch (DuplicateKeyException e) {
            // 동시 요청이 들어온 경우에도 기존 로그를 조회해서 복구 및 응답
            DailyQuestionLog duplicatedLog = dailyQuestionLogService.getTodayQuestionLog(memberId, today);

            if (duplicatedLog != null && duplicatedLog.getStatus() == DailyQuestionLogStatus.STARTED) {

                QuestionBank existingQuestion = questionBankService.getOpenQuestionById(duplicatedLog.getQuestionId());

                if (existingQuestion == null || !existingQuestion.getIsActive()) {

                    throw new BaseException(404, "현재 질문을 확인할 수 없습니다. 고객센터(☎010-1234-1234)로 문의 주시기 바랍니다.");
                }

                return new OpenQuestionStartResponse(
                        duplicatedLog.getDailyQuestionLogId(),
                        existingQuestion.getQuestionId(),
                        existingQuestion.getQuestionText(),
                        DailyQuestionLogStatus.STARTED
                );
            }

            throw new BaseException(409, "오늘은 이미 참여하였습니다.");
        }

        return new OpenQuestionStartResponse(
                dailyQuestionLog.getDailyQuestionLogId(),
                questionBank.getQuestionId(),
                questionBank.getQuestionText(),
                DailyQuestionLogStatus.STARTED
        );
    }

    @Override
    @Transactional
    public OpenQuestionExitResponse exitOpenQuestion(Long memberId, Long dailyQuestionLogId) {

        if (memberId == null || dailyQuestionLogId == null) {

            throw new BaseException(400, "잘못된 요청입니다.");
        }

        dailyQuestionLogService.delete(memberId, dailyQuestionLogId);

        return new OpenQuestionExitResponse(dailyQuestionLogId, DailyQuestionLogStatus.EXITED);
    }

    @Override
    @Transactional
    public OpenQuestionCompleteResponse completeOpenQuestion(Long memberId, Long dailyQuestionLogId, Integer responseSecond) {

        if (memberId == null || dailyQuestionLogId == null || responseSecond == null) {

            throw new BaseException(400, "잘못된 요청입니다.");
        }

        if (responseSecond < 10) {

            throw new BaseException(400, "10초 이상 생각한 후 답변해주세요.");
        }

        DailyQuestionLog dailyQuestionLog = DailyQuestionLog.builder()
                .dailyQuestionLogId(dailyQuestionLogId)
                .memberId(memberId)
                .responseSecond(responseSecond)
                .status(DailyQuestionLogStatus.COMPLETED)
                .build();

        dailyQuestionLogService.updateTodayQuestionLog(dailyQuestionLog);

        routineService.completeRoutineItem(memberId, MissionTypes.QUESTION);

        applicationEventPublisher.publishEvent(new CognitiveEvent(this, memberId, MetricScope.QUESTION));

        return new OpenQuestionCompleteResponse(dailyQuestionLogId, DailyQuestionLogStatus.COMPLETED);
    }
}

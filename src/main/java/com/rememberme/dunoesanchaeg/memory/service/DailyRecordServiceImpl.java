package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.memory.domain.DailyRecord;
import com.rememberme.dunoesanchaeg.memory.dto.request.DailyRecordSaveRequest;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordSaveResponse;
import com.rememberme.dunoesanchaeg.memory.mapper.DailyRecordMapper;
import com.rememberme.dunoesanchaeg.routines.domain.enums.MissionTypes;
import com.rememberme.dunoesanchaeg.routines.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordMapper dailyRecordMapper;

    private final RoutineService routineService;

    @Override
    public DailyRecordSaveResponse saveDailyRecord(Long memberId, DailyRecordSaveRequest request) {

        LocalDate today = LocalDate.now();

        DailyRecord dailyRecord = DailyRecord.builder()
                .memberId(memberId)
                .recordDate(today)
                .moodLevel(request.getMoodLevel())
                .moodMemo(request.getMoodMemo())
                .sleepLevel(request.getSleepLevel())
                .sleepMemo(request.getSleepMemo())
                .mealLevel(request.getMealLevel())
                .mealMemo(request.getMealMemo())
                .exerciseLevel(request.getExerciseLevel())
                .exerciseMemo(request.getExerciseMemo())
                .socialLevel(request.getSocialLevel())
                .socialMemo(request.getSocialMemo())
                .build();

        Long dailyRecordId = dailyRecordMapper.findDailyRecordId(memberId, today);

        int result;

        if (dailyRecordId == null) {
            result = dailyRecordMapper.insertDailyRecord(dailyRecord);
            if (result != 1) {
                throw new BaseException(500, "하루 기록 저장 실패");
            }
        } else {
            result = dailyRecordMapper.updateDailyRecord(dailyRecord);
            if (result != 1) {
                throw new BaseException(500, "하루 기록 수정 실패");
            }
        }

        routineService.completeRoutineItem(memberId, MissionTypes.RECORD);

        return new DailyRecordSaveResponse(
                today,
                request.getMoodLevel(),
                request.getMoodMemo(),
                request.getSleepLevel(),
                request.getSleepMemo(),
                request.getMealLevel(),
                request.getMealMemo(),
                request.getExerciseLevel(),
                request.getExerciseMemo(),
                request.getSocialLevel(),
                request.getSocialMemo()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public DailyRecordResponse getTodayDailyRecord(Long memberId) {
        LocalDate today = LocalDate.now();

        DailyRecord dailyRecord = dailyRecordMapper.selectDailyRecord(memberId, today);

        if (dailyRecord == null) {
            throw new BaseException(404, "오늘의 하루 기록이 존재하지 않습니다.");
        }

        return new DailyRecordResponse(
                dailyRecord.getRecordDate(),
                dailyRecord.getMoodLevel(),
                dailyRecord.getMoodMemo(),
                dailyRecord.getSleepLevel(),
                dailyRecord.getSleepMemo(),
                dailyRecord.getMealLevel(),
                dailyRecord.getMealMemo(),
                dailyRecord.getExerciseLevel(),
                dailyRecord.getExerciseMemo(),
                dailyRecord.getSocialLevel(),
                dailyRecord.getSocialMemo()
        );
    }
}
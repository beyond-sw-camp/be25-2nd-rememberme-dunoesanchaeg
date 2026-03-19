package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.common.exception.DailyRecordNotFoundException;
import com.rememberme.dunoesanchaeg.memory.domain.DailyRecord;
import com.rememberme.dunoesanchaeg.memory.dto.request.DailyRecordSaveRequest;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordSaveResponse;
import com.rememberme.dunoesanchaeg.memory.mapper.DailyRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordMapper dailyRecordMapper;

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

        if (dailyRecordId == null) {
            dailyRecordMapper.insertDailyRecord(dailyRecord);
        } else {
            dailyRecordMapper.updateDailyRecord(dailyRecord);
        }

        // dailyRecordMapper.updateRecordFinished(memberId, today);

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
    public DailyRecordResponse getTodayDailyRecord() {
        Long memberId = 1L;
        LocalDate today = LocalDate.now();

        DailyRecord dailyRecord = dailyRecordMapper.selectDailyRecord(memberId, today);

        if(dailyRecord == null){
            throw new DailyRecordNotFoundException();
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
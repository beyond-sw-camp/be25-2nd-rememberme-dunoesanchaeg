package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.memory.domain.DailyRecord;
import com.rememberme.dunoesanchaeg.memory.dto.request.DailyRecordSaveRequest;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordSaveResponse;
import com.rememberme.dunoesanchaeg.memory.mapper.DailyRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordMapper dailyRecordMapper;

    @Override
    public DailyRecordSaveResponse saveDailyRecord(Long memberId, DailyRecordSaveRequest request) {
        validateRequiredFields(request);

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

        dailyRecordMapper.updateRecordFinished(memberId, today);

        return new DailyRecordSaveResponse(today.toString(), true);
    }

    private void validateRequiredFields(DailyRecordSaveRequest request) {
        if (!StringUtils.hasText(request.getMoodLevel())) {
            throw new IllegalArgumentException("moodLevel은 필수입니다.");
        }
        if (!StringUtils.hasText(request.getSleepLevel())) {
            throw new IllegalArgumentException("sleepLevel은 필수입니다.");
        }
        if (!StringUtils.hasText(request.getMealLevel())) {
            throw new IllegalArgumentException("mealLevel은 필수입니다.");
        }
    }
}
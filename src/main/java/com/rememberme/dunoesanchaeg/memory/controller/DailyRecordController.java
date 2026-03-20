package com.rememberme.dunoesanchaeg.memory.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.memory.dto.request.DailyRecordSaveRequest;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordSaveResponse;
import com.rememberme.dunoesanchaeg.memory.service.DailyRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/daily-records")
@RequiredArgsConstructor
public class DailyRecordController {

    private final DailyRecordService dailyRecordService;

    @PutMapping
    public ApiResponse<DailyRecordSaveResponse> saveDailyRecord(
            @Valid @RequestBody DailyRecordSaveRequest request) {

        Long memberId = 1L; // JWT 연결 전 임시 테스트용
        DailyRecordSaveResponse response = dailyRecordService.saveDailyRecord(memberId, request);

        return ApiResponse.success(200, "하루 기록 저장 성공", response);
    }

    @GetMapping
    public ApiResponse<DailyRecordResponse> getTodayDailyRecord() {
        DailyRecordResponse response = dailyRecordService.getTodayDailyRecord();

        return ApiResponse.success(200, "오늘 하루 기록 조회 성공", response);
    }
}
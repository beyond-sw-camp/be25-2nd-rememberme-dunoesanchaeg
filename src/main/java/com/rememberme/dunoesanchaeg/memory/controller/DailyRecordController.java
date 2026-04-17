package com.rememberme.dunoesanchaeg.memory.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.memory.dto.request.DailyRecordSaveRequest;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordSaveResponse;
import com.rememberme.dunoesanchaeg.memory.service.DailyRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/daily-record")
@RequiredArgsConstructor
public class DailyRecordController {

    private final DailyRecordService dailyRecordService;

    @PutMapping
    public ResponseEntity<ApiResponse<DailyRecordSaveResponse>> saveDailyRecord(    // ApiResponse 구조 통일
            @Valid @RequestBody DailyRecordSaveRequest request) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        DailyRecordSaveResponse response = dailyRecordService.saveDailyRecord(memberId, request);

        return ResponseEntity.ok(ApiResponse.success(200, "하루 기록 저장 성공", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DailyRecordResponse>> getTodayDailyRecord() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        DailyRecordResponse response = dailyRecordService.getTodayDailyRecord(memberId);

        return ResponseEntity.ok(ApiResponse.success(200, "오늘 하루 기록 조회 성공", response));
    }
}
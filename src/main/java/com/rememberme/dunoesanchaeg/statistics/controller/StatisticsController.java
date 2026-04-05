package com.rememberme.dunoesanchaeg.statistics.controller;

// 1. Java 표준 및 서드파티 라이브러리 Import

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.statistics.dto.request.StatisticsRequest;
import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsResponse;
import com.rememberme.dunoesanchaeg.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {


    private final StatisticsService StatisticsService;

    @Operation(summary = "종목별 최근 7회 플레이 종합 통계 조회", description = "타겟 날짜를 기준으로 각 종목별 최근 7회 플레이 기록을 합산하여 조회합니다.")
    @GetMapping("/games/weekly-types")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getWeeklyTypeStatistics(
            @Valid @ModelAttribute StatisticsRequest request) {

        Long memberId = SecurityUtil.getCurrentMemberId();

        StatisticsResponse response =
                StatisticsService.getWeeklyTypeStatistics(memberId, request);

        return ResponseEntity.ok(
                ApiResponse.success(200, "종목별 최근 7회 플레이 종합 통계 조회를 성공했습니다.", response)
        );
    }
}
package com.rememberme.dunoesanchaeg.statistics.controller;


import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.statistics.dto.request.StatisticsRequest;
import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsResponse;
import com.rememberme.dunoesanchaeg.statistics.service.StatisticsService;
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

    private final StatisticsService statisticsService;

    @GetMapping("/games/weekly-types")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getWeeklyTypeStatistics(
            @Valid @ModelAttribute StatisticsRequest request
    ) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        StatisticsResponse response =
                statisticsService.getWeeklyTypeStatistics(memberId, request);

        return ResponseEntity.ok(
                ApiResponse.success(200, "종목별 최근 7회 플레이 통계 조회를 성공했습니다.", response)
        );
    }
}
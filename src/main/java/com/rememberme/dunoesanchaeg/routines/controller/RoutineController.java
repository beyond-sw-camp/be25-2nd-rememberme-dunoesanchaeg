package com.rememberme.dunoesanchaeg.routines.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.routines.dto.response.RoutineResponse;
import com.rememberme.dunoesanchaeg.routines.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/routines")
@RequiredArgsConstructor
public class RoutineController {
    private final RoutineService routineService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<RoutineResponse>> getTodayRoutine() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        RoutineResponse response = routineService.getTodayRoutine(memberId);

        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공", response));
    }
}
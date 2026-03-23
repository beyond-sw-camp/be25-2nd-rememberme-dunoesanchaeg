package com.rememberme.dunoesanchaeg.contents.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;
import com.rememberme.dunoesanchaeg.contents.service.CognitiveGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cognitive-games")
@RequiredArgsConstructor
public class CognitiveGameController {

    private final CognitiveGameService cognitiveGameService;

    @GetMapping("/today")
    public ApiResponse<TodayGameResponse> getTodayGame() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        TodayGameResponse response = cognitiveGameService.getTodayGame(memberId);
        return ApiResponse.success(200, "오늘의 미니게임 조회 성공", response);
    }
}

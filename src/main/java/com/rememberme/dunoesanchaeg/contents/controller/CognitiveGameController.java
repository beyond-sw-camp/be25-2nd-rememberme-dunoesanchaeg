package com.rememberme.dunoesanchaeg.contents.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.contents.dto.request.AnswerSubmitRequest;
import com.rememberme.dunoesanchaeg.contents.dto.response.GameFinishedResponse;
import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;
import com.rememberme.dunoesanchaeg.contents.service.CognitiveGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// update 쪽이랑 중간이탈처리 구현 필요

@RestController
@RequestMapping("/api/v1/cognitive-games")
@RequiredArgsConstructor
public class CognitiveGameController {

    private final CognitiveGameService cognitiveGameService;

    // 오늘 게임 조회
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<TodayGameResponse>> getTodayGame() {
        Long memberId = SecurityUtil.getCurrentMemberId();

        TodayGameResponse response =
                cognitiveGameService.getTodayGame(memberId);
        return ResponseEntity.ok(ApiResponse.success(200, "오늘의 미니게임 조회 성공", response));
    }

    // 2. 게임 결과 저장
    @PostMapping("/result")
    public ResponseEntity<ApiResponse<GameFinishedResponse>> saveGameResult(@Valid @RequestBody AnswerSubmitRequest request) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        GameFinishedResponse response = cognitiveGameService.saveGameResult(memberId, request);

        return ResponseEntity.ok(ApiResponse.success(200, "미니게임 결과 저장 성공", response));
    }
}

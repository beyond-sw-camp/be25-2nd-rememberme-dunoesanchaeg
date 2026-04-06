package com.rememberme.dunoesanchaeg.memory.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.memory.dto.request.OpenQuestionCompleteRequest;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionCompleteResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionExitResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionStartResponse;
import com.rememberme.dunoesanchaeg.memory.service.OpenQuestionRoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/open-questions")
@RequiredArgsConstructor
@Tag(name = "Open Question APIs", description = "개방형질문 관련 API 목록")
public class OpenQuestionController {
    private final OpenQuestionRoutineService openQuestionRoutineService;

    @PostMapping("/start")
    @Operation(summary = "개방형질문 시작")
    public ResponseEntity<ApiResponse<OpenQuestionStartResponse>> startOpenQuestion() {

        Long memberId = SecurityUtil.getCurrentMemberId();
        OpenQuestionStartResponse response = openQuestionRoutineService.startOpenQuestion(memberId);

        return ResponseEntity.ok(ApiResponse.success(200, "개방형질문 루틴을 시작하였습니다.", response));
    }

    @DeleteMapping("/{daily-question-log-id}")
    @Operation(summary = "개방형질문 이탈")
    public ResponseEntity<ApiResponse<OpenQuestionExitResponse>> exitOpenQuestion(
            @PathVariable("daily-question-log-id") Long dailyQuestionLogId) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        OpenQuestionExitResponse response = openQuestionRoutineService.exitOpenQuestion(memberId, dailyQuestionLogId);

        return ResponseEntity.ok(ApiResponse.success(200, "개방형질문 루틴을 이탈하였습니다.", response));
    }

    @PatchMapping("/{daily-question-log-id}")
    @Operation(summary = "개방형질문 완료")
    public ResponseEntity<ApiResponse<OpenQuestionCompleteResponse>> completeOpenQuestion(
            @PathVariable("daily-question-log-id") Long dailyQuestionLogId,
            @Valid @RequestBody OpenQuestionCompleteRequest request) {

        Long memberId = SecurityUtil.getCurrentMemberId();
        OpenQuestionCompleteResponse response = openQuestionRoutineService.completeOpenQuestion(memberId, dailyQuestionLogId, request.getResponseSecond());

        return ResponseEntity.ok(ApiResponse.success(200, "개방형질문 루틴을 완료하였습니다.", response));
    }
}
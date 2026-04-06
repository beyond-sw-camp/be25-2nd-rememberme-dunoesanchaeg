package com.rememberme.dunoesanchaeg.support.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.support.dto.response.NoticeDetailResponse;
import com.rememberme.dunoesanchaeg.support.dto.response.NoticeListResponse;
import com.rememberme.dunoesanchaeg.support.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NoticeListResponse>>> searchNotice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<NoticeListResponse> noticeListResponses = noticeService.searchNotice(page, size);
        return ResponseEntity.ok(ApiResponse.success(200, "공지사항 조회 성공", noticeListResponses));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> searchNoticeDetail(
            @PathVariable Long noticeId
    ){
        NoticeDetailResponse noticeDetailResponse = noticeService.searchNoticeDetail(noticeId);
        return ResponseEntity.ok(ApiResponse.success(200, "공지사항 상세 조회 성공", noticeDetailResponse));
    }
}

package com.rememberme.dunoesanchaeg.member.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.member.dto.request.AdditionalInfoRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.AdditionalInfoResponse;
import com.rememberme.dunoesanchaeg.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PutMapping("/profile")
    ApiResponse<AdditionalInfoResponse> addProfile(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody AdditionalInfoRequest request
    ){
        log.info("추가 프로필 요청 memberId: {}", memberId);
        AdditionalInfoResponse response = memberService.completeProfile(memberId, request);


        return ApiResponse.success(200, "프로필 추가 성공", response);
    }
}

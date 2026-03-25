package com.rememberme.dunoesanchaeg.member.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import com.rememberme.dunoesanchaeg.member.dto.request.AdditionalInfoRequest;
import com.rememberme.dunoesanchaeg.member.dto.request.UpdateMemberRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.AdditionalInfoResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.RetrieveMemberResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.UpdateMemberResponse;
import com.rememberme.dunoesanchaeg.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PutMapping("/profile")
    ResponseEntity<ApiResponse<AdditionalInfoResponse>> addProfile(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody AdditionalInfoRequest request
    ){
        log.info("추가 프로필 요청 memberId: {}", memberId);
        AdditionalInfoResponse response = memberService.completeProfile(memberId, request);


        return ResponseEntity.ok(ApiResponse.success(200, "프로필 추가 성공", response));
    }

    @GetMapping("/me")
    ResponseEntity<ApiResponse<RetrieveMemberResponse>> retrieve(
            @AuthenticationPrincipal Long memberId
    ){
        log.info("memberId : {} 정보조회", memberId);
        RetrieveMemberResponse response = memberService.retrieveMember(memberId);
        if (UserStatus.WITHDRAWN.equals(response.getUserStatus())) {
            return ResponseEntity.ok(ApiResponse.success(200, "탈퇴한 회원입니다. 30일 이내 복구 가능합니다.", response));
        }

        return ResponseEntity.ok(ApiResponse.success(200, "프로필 조회 성공", response));
    }

    @PatchMapping("/me")
    ResponseEntity<ApiResponse<UpdateMemberResponse>> update(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody UpdateMemberRequest request
            ){
        log.info("memberId : {} 정보수정", memberId);
        UpdateMemberResponse response = memberService.updateMember(memberId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "프로필 수정 성공", response));
    }
}

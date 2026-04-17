package com.rememberme.dunoesanchaeg.member.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.CookieUtil;
import com.rememberme.dunoesanchaeg.common.security.JwtProvider;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import com.rememberme.dunoesanchaeg.member.dto.request.AdditionalInfoRequest;
import com.rememberme.dunoesanchaeg.member.dto.request.RecoveryRequest;
import com.rememberme.dunoesanchaeg.member.dto.request.UpdateMemberRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.AdditionalInfoResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.RecoveryResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.SearchMemberResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.UpdateMemberResponse;
import com.rememberme.dunoesanchaeg.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;

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
    ResponseEntity<ApiResponse<SearchMemberResponse>> search(
            @AuthenticationPrincipal Long memberId
    ){
        log.info("memberId : {} 정보조회", memberId);
        SearchMemberResponse response = memberService.searchMember(memberId);
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

    @DeleteMapping("/me")
    ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal Long memberId,
            HttpServletResponse response
    ){
        log.info("memberId : {} 탈퇴", memberId);
        memberService.withdrawMember(memberId);

        ResponseCookie cookie = cookieUtil.deleteRefreshTokenCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiResponse.success(200, "회원탈퇴에 성공했습니다. 30일 이내 복구 가능합니다.", null));
    }

    @PostMapping("/me/recovery")
    ResponseEntity<ApiResponse<RecoveryResponse>> recovery(
            @AuthenticationPrincipal Long memberId,
            @RequestHeader("User-Agent") String userAgent,
            @Valid @RequestBody RecoveryRequest request,
            HttpServletResponse response
    ){
        log.info("memberId : {} 복구", memberId);
        RecoveryResponse recoveryResponse = memberService.recoveryMember(memberId, request, userAgent);

        // 쿠키 저장
        ResponseCookie cookie = cookieUtil.createRefreshTokenCookie(recoveryResponse.getRefreshToken(), jwtProvider.getRefreshTokenStepSeconds());

        // 쿠키 등록
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(ApiResponse.success(200, "회원 복구에 성공했습니다.", recoveryResponse));
    }
}

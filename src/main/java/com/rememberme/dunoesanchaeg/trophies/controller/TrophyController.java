package com.rememberme.dunoesanchaeg.trophies.controller;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import com.rememberme.dunoesanchaeg.trophies.dto.response.TrophyListResponse;
import com.rememberme.dunoesanchaeg.trophies.service.TrophyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trophies")
public class TrophyController {

	private final TrophyService trophyService;

	@GetMapping
	public ResponseEntity<ApiResponse<TrophyListResponse>> getMyTrophies() {

		Long memberId = SecurityUtil.getCurrentMemberId();

		TrophyListResponse response = trophyService.getMyTrophies(memberId);

		return ResponseEntity.ok(ApiResponse.success(200, "트로피 내역 조회를 성공했습니다.", response)
		);
	}
}

package com.rememberme.dunoesanchaeg.calendar.controller;

import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarSummaryRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarSummaryResponse;
import com.rememberme.dunoesanchaeg.calendar.service.CalendarSummaryService;
import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calendar")
public class CalendarController {

	private final CalendarSummaryService calendarSummaryService;

	@GetMapping("/summary")
	public ResponseEntity<ApiResponse<CalendarSummaryResponse>> getCalendarSummary(
			@Valid @ModelAttribute CalendarSummaryRequest request
	) {
		Long memberId = SecurityUtil.getCurrentMemberId();

		CalendarSummaryResponse response =
				calendarSummaryService.getCalendarSummary(memberId, request);

		return ResponseEntity.ok(
				ApiResponse.success(200, "일간 종합 기록 조회를 성공했습니다.", response)
		);
	}
}
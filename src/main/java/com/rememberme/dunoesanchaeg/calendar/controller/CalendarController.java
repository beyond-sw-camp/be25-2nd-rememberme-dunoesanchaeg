package com.rememberme.dunoesanchaeg.calendar.controller;

import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarMonthRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarSummaryRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarMonthResponse;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarSummaryResponse;
import com.rememberme.dunoesanchaeg.calendar.service.CalendarSummaryService;
import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(
			summary = "월간 루틴 완료 일자 조회",
			description = "요청한 날짜가 속한 달에서 루틴을 1개라도 완료한 날짜 목록을 조회합니다."
	)
	@GetMapping("/completed-days")
	public ResponseEntity<ApiResponse<CalendarMonthResponse>> getMonthlyCompletedRoutineDays(
			@Valid @ModelAttribute CalendarMonthRequest request
	) {
		Long memberId = SecurityUtil.getCurrentMemberId();

		CalendarMonthResponse response =
				calendarSummaryService.getMonthlyCompletedRoutineDays(memberId, request);

		return ResponseEntity.ok(
				ApiResponse.success(200, "월간 루틴 완료 일자 조회를 성공했습니다.", response)
		);
	}
}

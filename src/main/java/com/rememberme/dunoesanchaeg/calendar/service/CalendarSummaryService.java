package com.rememberme.dunoesanchaeg.calendar.service;

import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarMonthRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarSummaryRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarMonthResponse;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarSummaryResponse;

public interface CalendarSummaryService {

	CalendarSummaryResponse getCalendarSummary(Long memberId, CalendarSummaryRequest request);

	CalendarMonthResponse getMonthlyCompletedRoutineDays(Long memberId, CalendarMonthRequest request);
}
package com.rememberme.dunoesanchaeg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CalendarMonthResponse {

	@JsonProperty("target_month")
	private String targetMonth;

	@JsonProperty("completed_dates")
	private List<String> completedDates;
}
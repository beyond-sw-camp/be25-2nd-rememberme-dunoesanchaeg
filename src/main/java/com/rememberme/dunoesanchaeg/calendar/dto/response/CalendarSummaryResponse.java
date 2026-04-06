package com.rememberme.dunoesanchaeg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CalendarSummaryResponse {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate targetDate;

	@JsonProperty("progress_rate")
	private Integer progressRate;

	@JsonProperty("game_record")
	private GameRecord gameRecord;

	@JsonProperty("question_record")
	private QuestionRecord questionRecord;

	@JsonProperty("daily_record")
	private DailyRecordDetail dailyRecord;

}
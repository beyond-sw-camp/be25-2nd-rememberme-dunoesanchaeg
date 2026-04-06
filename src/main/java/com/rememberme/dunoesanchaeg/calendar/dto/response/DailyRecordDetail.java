package com.rememberme.dunoesanchaeg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.memory.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DailyRecordDetail {

	@JsonProperty("is_written")
	private Boolean isWritten;

	@JsonProperty("mood_level")
	private Level moodLevel;

	@JsonProperty("mood_memo")
	private String moodMemo;

	@JsonProperty("sleep_level")
	private Level sleepLevel;

	@JsonProperty("sleep_memo")
	private String sleepMemo;

	@JsonProperty("meal_level")
	private Level mealLevel;

	@JsonProperty("meal_memo")
	private String mealMemo;

	@JsonProperty("exercise_level")
	private Level exerciseLevel;

	@JsonProperty("exercise_memo")
	private String exerciseMemo;

	@JsonProperty("social_level")
	private Level socialLevel;

	@JsonProperty("social_memo")
	private String socialMemo;
}
package com.rememberme.dunoesanchaeg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.memory.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyRecordDetail {

	@JsonProperty("is_written")
	private Boolean isWritten;

	@JsonProperty("sleep_level")
	private Level sleepLevel;

	@JsonProperty("mood_level")
	private Level moodLevel;

	@JsonProperty("meal_level")
	private Level mealLevel;
}
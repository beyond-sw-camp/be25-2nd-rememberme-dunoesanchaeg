package com.rememberme.dunoesanchaeg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GameRecord {

	@JsonProperty("is_played")
	private Boolean isPlayed;

	@JsonProperty("play_time_seconds")
	private Integer playTimeSeconds;

	@JsonProperty("correct_count")
	private Integer correctCount;
}
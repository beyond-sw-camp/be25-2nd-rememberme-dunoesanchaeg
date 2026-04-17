package com.rememberme.dunoesanchaeg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GameRecord {

    @JsonProperty("is_played")
    private Boolean isPlayed;

    @JsonProperty("play_time_seconds")
    private Integer totalPlayedTime;

    @JsonProperty("correct_count")
    private Integer correctCount;
}
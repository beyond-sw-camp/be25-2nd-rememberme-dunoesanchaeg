package com.rememberme.dunoesanchaeg.statistics.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.statistics.domain.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsItemResponse {

    @JsonProperty("game_type")
    private GameType gameType;

    @JsonProperty("game_name")
    private String gameName;

    @JsonProperty("play_count")
    private Integer playCount;

    @JsonProperty("total_questions")
    private Integer totalQuestions;

    @JsonProperty("total_correct")
    private Integer totalCorrect;

    @JsonProperty("accuracy")
    private Integer accuracy;

    @JsonProperty("scores")
    private List<Integer> scores;
}
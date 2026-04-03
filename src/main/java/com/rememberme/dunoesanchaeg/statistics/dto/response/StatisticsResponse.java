package com.rememberme.dunoesanchaeg.statistics.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StatisticsResponse {

    @JsonProperty("target_date")
    private String targetDate;

    @JsonProperty("stats")
    private List<StatisticsItemResponse> stats;
}
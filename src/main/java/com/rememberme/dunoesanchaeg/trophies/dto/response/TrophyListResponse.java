package com.rememberme.dunoesanchaeg.trophies.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrophyListResponse {

	@JsonProperty("total_count")
	private Integer totalCount;

	@JsonProperty("trophy_list")
	private List<TrophyItemResponse> trophyList;
}
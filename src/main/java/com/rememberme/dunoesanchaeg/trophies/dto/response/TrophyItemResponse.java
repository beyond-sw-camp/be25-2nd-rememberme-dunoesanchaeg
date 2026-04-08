package com.rememberme.dunoesanchaeg.trophies.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.trophies.domain.Trophy;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TrophyItemResponse {

	@JsonProperty("trophy_id")
	private Long trophyId;

	@JsonProperty("trophy_name")
	private String trophyName;

	@JsonProperty("acquired_at")
	private LocalDateTime acquiredAt;

	public static TrophyItemResponse trophyItemResponse(Trophy trophy) {
		return TrophyItemResponse.builder()
				.trophyId(trophy.getTrophyId())
				.trophyName(trophy.getTrophyName())
				.acquiredAt(trophy.getAcquiredAt())
				.build();
	}
}
package com.rememberme.dunoesanchaeg.trophies.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rememberme.dunoesanchaeg.trophies.domain.Trophy;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class TrophyItemResponse {

	@JsonProperty("trophy_id")
	private Long trophyId;

	@JsonProperty("trophy_type")
	private String trophyType;

	@JsonProperty("trophy_name")
	private String trophyName;

	@JsonProperty("acquired_at")
	private OffsetDateTime acquiredAt;

	public static TrophyItemResponse trophyItemResponse(Trophy trophy) {
		return TrophyItemResponse.builder()
				.trophyId(trophy.getTrophyId())
				.trophyType(trophy.getTrophyType())
				.trophyName(trophy.getTrophyName())
				.acquiredAt(trophy.getAcquiredAt())
				.build();
	}
	private static String resolveTrophyName(String trophyType) {
		if (trophyType == null || !trophyType.startsWith("total_routine_count_")) {
			return "";
		}

		String countText = trophyType.replace("total_routine_count_", "");
		return countText + "일 루틴 달성 기념";
	}
}
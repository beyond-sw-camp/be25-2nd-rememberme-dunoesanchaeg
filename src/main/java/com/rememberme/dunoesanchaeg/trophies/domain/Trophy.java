package com.rememberme.dunoesanchaeg.trophies.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Trophy {

	private Long trophyId;
	private Long memberId;
	private String trophyType;
	private String trophyName;
	private OffsetDateTime acquiredAt;

	@Builder
	public Trophy(Long trophyId, Long memberId, String trophyType, String trophyName, OffsetDateTime acquiredAt) {
		this.trophyId = trophyId;
		this.memberId = memberId;
		this.trophyType = trophyType;
		this.trophyName = trophyName;
		this.acquiredAt = acquiredAt;
	}
}
package com.rememberme.dunoesanchaeg.trophies.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Trophy {

	private Long trophyId;
	private Long memberId;
	private String trophyName;
	private LocalDateTime acquiredAt;

}
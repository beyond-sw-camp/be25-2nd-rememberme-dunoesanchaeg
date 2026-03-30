package com.rememberme.dunoesanchaeg.trophies.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum TrophyPolicy {

	ROUTINE_10(10, "total_routine_count_10", "10일 루틴 달성 기념"),
	ROUTINE_20(20, "total_routine_count_20", "20일 루틴 달성 기념"),
	ROUTINE_30(30, "total_routine_count_30", "30일 루틴 달성 기념"),
	ROUTINE_40(40, "total_routine_count_40", "40일 루틴 달성 기념"),
	ROUTINE_50(50, "total_routine_count_50", "50일 루틴 달성 기념");

	private final int targetCount;
	private final String trophyType;
	private final String trophyName;

	public static Optional<TrophyPolicy> findByTargetCount(int totalCount) {
		return Arrays.stream(values())
				.filter(policy -> policy.targetCount == totalCount)
				.findFirst();
	}
}
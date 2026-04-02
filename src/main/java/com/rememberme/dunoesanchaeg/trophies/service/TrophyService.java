package com.rememberme.dunoesanchaeg.trophies.service;


import com.rememberme.dunoesanchaeg.trophies.dto.response.TrophyListResponse;

public interface TrophyService {

	TrophyListResponse getMyTrophies(Long memberId);

	void awardRoutineCountTrophy(Long memberId);
}
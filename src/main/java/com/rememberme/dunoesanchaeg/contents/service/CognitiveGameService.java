package com.rememberme.dunoesanchaeg.contents.service;

import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;

public interface CognitiveGameService {
    TodayGameResponse getTodayGame(Long memberId);
}

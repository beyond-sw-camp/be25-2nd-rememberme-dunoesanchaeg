package com.rememberme.dunoesanchaeg.contents.service;

import com.rememberme.dunoesanchaeg.contents.dto.request.AnswerSubmitRequest;
import com.rememberme.dunoesanchaeg.contents.dto.response.GameFinishedResponse;
import com.rememberme.dunoesanchaeg.contents.dto.response.TodayGameResponse;

public interface CognitiveGameService {
    TodayGameResponse getTodayGame(Long memberId);
    GameFinishedResponse saveGameResult(Long memberId, AnswerSubmitRequest request);
}

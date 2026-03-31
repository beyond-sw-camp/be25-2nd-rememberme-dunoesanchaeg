package com.rememberme.dunoesanchaeg.contents.dto.request;

import com.rememberme.dunoesanchaeg.contents.domain.enums.GameType;

public class AnswerSubmitRequest {
    private GameType gameType;
    private int correctCount;
    private int totalTryCount;
}

package com.rememberme.dunoesanchaeg.contents.dto.response;

import com.rememberme.dunoesanchaeg.contents.domain.enums.GameType;

import java.time.LocalDate;

public class TodayGameResponse {
    private LocalDate playedDate;
    private GameType gameType;
    private int totalRounds;
    private int roundTimeLimitSec;
    private String passCondition;
    private boolean isGameFinished;
}

package com.rememberme.dunoesanchaeg.contents.dto.response;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayGameResponse {
    private LocalDate playedDate;
    private AssignedGameType gameType;
    private int totalRounds;
    private int roundTimeLimitSec;
    private String passCondition;
    private Boolean isGameFinished;
}

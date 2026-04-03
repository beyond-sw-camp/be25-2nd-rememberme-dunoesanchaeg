package com.rememberme.dunoesanchaeg.statistics.domain;

import com.rememberme.dunoesanchaeg.statistics.domain.enums.GameType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameStatistic {
    private GameType gameType;
    private Integer playCount;
    private Integer totalQuestions;
    private Integer totalCorrect;
}

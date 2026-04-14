package com.rememberme.dunoesanchaeg.contents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameFinishedResponse {
    private Integer correctCount;
    private Integer wrongCount;
    private Integer timeoutCount;
    private Integer totalTryCount;
    private Integer totalPlayedTime;
    private Integer totalRounds;
    private Boolean isGameFinished;
    private Boolean isValid;
}

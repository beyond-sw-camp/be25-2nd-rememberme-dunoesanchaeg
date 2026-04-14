package com.rememberme.dunoesanchaeg.contents.dto.request;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GameResultInsertDto {
    private Long memberId;
    private LocalDate playedDate;
    private AssignedGameType gameType;
    private int correctCount;
    private int wrongCount;
    private int timeoutCount;
    private int totalTryCount;
    private int totalPlayedTime;
    private boolean isValid;
}
package com.rememberme.dunoesanchaeg.contents.domain;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyGameLog {
    private Long sessionId;
    private Long memberId;
    private LocalDate playedDate;
    private AssignedGameType gameType;
    private Integer totalTryCount;
    private Integer correctCount;
    private Integer playTimeSeconds;
    private Boolean alertTriggered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isValid;
}

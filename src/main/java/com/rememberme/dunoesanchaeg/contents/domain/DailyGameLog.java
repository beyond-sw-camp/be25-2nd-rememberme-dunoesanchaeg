package com.rememberme.dunoesanchaeg.contents.domain;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyGameLog {
    private Long sessionId;
    private Long memberId;
    private LocalDate playedDate;
    private AssignedGameType gameType;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer timeoutCount;
    private Integer totalTryCount;
    private Integer totalPlayedTime;
    private Boolean alertTriggered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isValid;
}

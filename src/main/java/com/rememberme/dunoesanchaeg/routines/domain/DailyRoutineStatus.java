package com.rememberme.dunoesanchaeg.routines.domain;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import com.rememberme.dunoesanchaeg.routines.domain.enums.MissionTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyRoutineStatus {
    private MissionTypes missionTypes; // GAME, RECORD, QUESTION

    private Long routineId;
    private Long memberId;
    private LocalDate routineDate;

    private AssignedGameType assignedGameType;
    private Long assignedQuestionId;

    private Boolean isGameFinished;
    private Boolean isRecordFinished;
    private Boolean isQuestionFinished;
    private LocalDateTime createdAt;
}

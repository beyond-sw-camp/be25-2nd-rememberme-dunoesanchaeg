package com.rememberme.dunoesanchaeg.routines.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class RoutineResponse {

    private String username;

    private MissionTypes missionTypes; // GAME, RECORD, QUESTION

    private Long routineId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate routineDate;

    private String assignedGameType;
    private Long assignedQuestionId;

    private boolean isGameFinished;
    private boolean isRecordFinished;
    private boolean isQuestionFinished;

    private int completedCnt;
    private int progressRate;
    private String feedbackMsg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
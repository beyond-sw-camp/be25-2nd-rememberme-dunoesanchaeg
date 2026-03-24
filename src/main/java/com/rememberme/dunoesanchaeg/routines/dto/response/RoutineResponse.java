package com.rememberme.dunoesanchaeg.routines.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineResponse {

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
}
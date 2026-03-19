package com.rememberme.dunoesanchaeg.memory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyRecordSaveResponse {

    private String recordDate;
    private boolean isRecordFinished;
}
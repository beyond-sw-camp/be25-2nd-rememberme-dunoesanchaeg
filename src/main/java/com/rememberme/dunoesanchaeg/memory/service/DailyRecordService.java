package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.memory.dto.request.DailyRecordSaveRequest;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.DailyRecordSaveResponse;

public interface DailyRecordService {

    DailyRecordResponse getTodayDailyRecord();

    DailyRecordSaveResponse saveDailyRecord(Long memberId, DailyRecordSaveRequest request);
}
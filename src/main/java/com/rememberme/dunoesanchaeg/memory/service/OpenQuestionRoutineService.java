package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionCompleteResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionExitResponse;
import com.rememberme.dunoesanchaeg.memory.dto.response.OpenQuestionStartResponse;

// 개방형질문 서비스 담당
public interface OpenQuestionRoutineService {

    OpenQuestionStartResponse startOpenQuestion(Long memberId);

    OpenQuestionExitResponse exitOpenQuestion(Long memberId, Long dailyQuestionLogId);

    OpenQuestionCompleteResponse completeOpenQuestion(Long memberId, Long dailyQuestionLogId, Integer responseSecond);
}

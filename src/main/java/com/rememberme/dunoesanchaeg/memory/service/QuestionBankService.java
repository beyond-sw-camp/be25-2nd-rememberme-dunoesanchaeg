package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.memory.domain.QuestionBank;

// 개방형질문 내용 조회 담당
public interface QuestionBankService {

    QuestionBank getOpenQuestionById(Long questionId);
}

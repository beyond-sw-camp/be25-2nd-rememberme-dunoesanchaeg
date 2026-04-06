package com.rememberme.dunoesanchaeg.memory.service;

import com.rememberme.dunoesanchaeg.memory.domain.QuestionBank;
import com.rememberme.dunoesanchaeg.memory.mapper.QuestionBankMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBankServiceImpl implements QuestionBankService {
    private final QuestionBankMapper questionBankMapper;

    @Override
    public QuestionBank getOpenQuestionById(Long questionId) {

        return questionBankMapper.selectOpenQuestionById(questionId);
    }
}

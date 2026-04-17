package com.rememberme.dunoesanchaeg.memory.mapper;

import com.rememberme.dunoesanchaeg.memory.domain.QuestionBank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionBankMapper {

    // 개방형질문 선택
    QuestionBank selectOpenQuestionById(@Param("questionId") Long questionId);
}

package com.rememberme.dunoesanchaeg.memory.domain;

import com.rememberme.dunoesanchaeg.memory.domain.enums.QuestionBankCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBank {
    private Long questionId;

    private String questionText;

    private QuestionBankCategory questionBankCategory;

    private Boolean isActive;

    private LocalDateTime createdAt;
}

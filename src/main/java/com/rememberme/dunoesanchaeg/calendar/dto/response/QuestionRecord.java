package com.rememberme.dunoesanchaeg.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QuestionRecord {

	@JsonProperty("is_answered")
	private Boolean isAnswered;

	@JsonProperty("question_text")
	private String questionText;
}
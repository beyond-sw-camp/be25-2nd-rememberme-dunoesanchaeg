package com.rememberme.dunoesanchaeg.calendar.dto.request;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
@Setter
@NoArgsConstructor
public class CalendarSummaryRequest {

	@NotBlank(message = "필수 파라미터입니다. (YYYY-MM-DD 형식으로 입력해주세요.)")
	@Pattern(
			regexp = "^\\d{4}-\\d{2}-\\d{2}$",
			message = "필수 파라미터입니다. (YYYY-MM-DD 형식으로 입력해주세요.)"
	)
	private String targetDate;

	public LocalDate toLocalDate() {
		try {
			return LocalDate.parse(targetDate);
		} catch (DateTimeParseException e) {
			throw new BaseException(400, "잘못된 요청입니다. 입력값을 확인해주세요.");
		}
	}
}
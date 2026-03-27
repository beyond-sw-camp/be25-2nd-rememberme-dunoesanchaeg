package com.rememberme.dunoesanchaeg.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String field;
    private final String reason;

    public static ErrorResponse of(FieldError fieldError) {
        return ErrorResponse.builder()
                .field(fieldError.getField())
                .reason(fieldError.getDefaultMessage())
                .build();
    }
}

package com.rememberme.dunoesanchaeg.common;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 우리가 직접 던지는 비즈니스 에러 (탈퇴 회원 등)
    @ExceptionHandler(BaseException.class)
    public ApiResponse<Void> handleBaseException(BaseException e) {
        log.warn("비즈니스 예외 발생: {}", e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    // 2. @Valid 검증 실패 에러 (아이디 누락 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException e) {
        List<ErrorResponse> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        log.warn("검증 실패 에러 발생: {}건", errors.size());

        // 2) ApiResponse.fail에 에러 리스트를 실어서 보냄
        return ApiResponse.fail(400, "입력 데이터가 유효하지 않습니다.", errors);
    }

    // 3. 그 외 예상치 못한 모든 서버 에러
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("예상치 못한 서버 오류 발생: ", e);
        return ApiResponse.error(500, "서버 내부 오류가 발생했습니다.");
    }

}
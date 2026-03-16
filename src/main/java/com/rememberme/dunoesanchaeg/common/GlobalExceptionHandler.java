package com.rememberme.dunoesanchaeg.common.handler;

import com.rememberme.dunoesanchaeg.common.ApiResponse;
import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 우리가 직접 던지는 비즈니스 에러 (탈퇴 회원 등)
    @ExceptionHandler(BaseException.class)
    public ApiResponse<Void> handleBaseException(BaseException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    // 2. @Valid 검증 실패 에러 (아이디 누락 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        return ApiResponse.error(400, errorMessage);
    }

    // 3. 그 외 예상치 못한 모든 서버 에러
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        return ApiResponse.error(500, "서버 내부 오류가 발생했습니다.");
    }
}
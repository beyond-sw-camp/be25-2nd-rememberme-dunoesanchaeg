package com.rememberme.dunoesanchaeg.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    // 성공 응답 (데이터가 있는 경우)
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    // 성공 응답 (데이터가 없는 경우, 보통 삭제나 업데이트 성공 시)
    public static <T> ApiResponse<T> success(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // 실패 응답 (데이터가 있는 경우)
    public static <T> ApiResponse<T> fail(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

}
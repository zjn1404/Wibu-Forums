package com.nqt.identity_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nqt.identity_service.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException() {
        ErrorCode uncategorizedError = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return ResponseEntity.status(uncategorizedError.httpStatus)
                .body(ApiResponse.builder()
                        .code(uncategorizedError.code)
                        .message(uncategorizedError.message)
                        .build());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException appException) {
        return ResponseEntity.status(appException.getErrorCode().httpStatus)
                .body(ApiResponse.builder()
                        .code(appException.getErrorCode().code)
                        .message(appException.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException() {
        ErrorCode unauthorizedError = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(unauthorizedError.httpStatus)
                .body(ApiResponse.builder()
                        .code(unauthorizedError.code)
                        .message(unauthorizedError.message)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(exception.getFieldError().getDefaultMessage());
        } catch (Exception ignored) {
        }

        return ResponseEntity.status(errorCode.httpStatus)
                .body(ApiResponse.builder()
                        .code(errorCode.code)
                        .message(errorCode.message)
                        .build());
    }
}

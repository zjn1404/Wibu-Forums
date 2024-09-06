package com.nqt.profile_service.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Undefined error
    UNCATEGORIZED_EXCEPTION(9999, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized Error"),

    // Developer error (1xxx)
    INVALID_KEY(1001, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error"),

    // Client error
    // Input error (2xxx)

    // Existed error (25xx)
    USER_PROFILE_EXISTED(2505, HttpStatus.BAD_REQUEST, "User profile already existed"),

    // Other (28xx)
    // Unauthenticated error (3xxx)
    UNAUTHENTICATED(3001, HttpStatus.UNAUTHORIZED, "Authentication Failed"),
    INVALID_TOKEN(3002, HttpStatus.UNAUTHORIZED, "Invalid Token"),
    // Unauthorized error (4xxx)
    UNAUTHORIZED(4001, HttpStatus.FORBIDDEN, "Don't have permission"),

    // Not Found error (5xxx)
    USER_PROFILE_NOT_FOUND(5004, HttpStatus.NOT_FOUND, "User profile not found"),
    ;

    final int code;
    final HttpStatus httpStatus;
    final String message;
}

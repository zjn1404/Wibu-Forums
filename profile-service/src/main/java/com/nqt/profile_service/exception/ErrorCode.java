package com.nqt.profile_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
    USER_PROFILE_EXISTED(25001, HttpStatus.BAD_REQUEST, "User profile already existed"),

    // Other (2xxx)

    // Not Found error (5xxx)
    USER_PROFILE_NOT_FOUND(5001, HttpStatus.BAD_REQUEST, "User profile not found"),
    ;

    final int code;
    final HttpStatus httpStatus;
    final String message;
}

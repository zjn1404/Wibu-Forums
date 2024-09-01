package com.nqt.post_service.exception;

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
    // Other (28xx)
    // Unauthenticated error (3xxx)
    UNAUTHENTICATED(3001, HttpStatus.UNAUTHORIZED, "Authentication Failed"),
    INVALID_TOKEN(3002, HttpStatus.UNAUTHORIZED, "Invalid Token"),
    ACCOUNT_NOT_VERIFIED(3003, HttpStatus.UNAUTHORIZED, "Account Not Verified"),
    PASSWORD_INCORRECT(3004, HttpStatus.UNAUTHORIZED, "Password Incorrect"),
    // Unauthorized error (4xxx)
    UNAUTHORIZED(4001, HttpStatus.FORBIDDEN, "Don't have permission"),
    // Not Found error (5xxx)
    USER_NOT_EXISTED(5001, HttpStatus.NOT_FOUND, "User Not Existed"),
    ;

    final int code;
    final HttpStatus httpStatus;
    final String message;
}

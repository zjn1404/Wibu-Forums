package com.nqt.identity_service.exception;

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
    USERNAME_INVALID(2001, HttpStatus.BAD_REQUEST, "Username must be at least 6 characters"),
    PASSWORD_INVALID(2002, HttpStatus.BAD_REQUEST, "Password must be at least 8 characters"),
    THE_SAME_PASSWORD(2003, HttpStatus.BAD_REQUEST, "New Password Is The Same As Old Password"),
    VERIFY_CODE_INCORRECT(2004, HttpStatus.BAD_REQUEST, "Verify Code Incorrect"),
    PHONE_NUMBER_INVALID(2005, HttpStatus.BAD_REQUEST, "Phone Number Is Not Valid"),
    // Existed error (25xx)
    USER_EXISTED(2501, HttpStatus.BAD_REQUEST, "User Existed"),
    ROLE_EXISTED(2502, HttpStatus.BAD_REQUEST, "Role Existed"),
    PERMISSION_EXISTED(2503, HttpStatus.BAD_REQUEST, "Permission Existed"),
    PASSWORD_EXISTED(2504, HttpStatus.BAD_REQUEST, "Password Existed"),
    // Other (28xx)
    VERIFY_CODE_EXPIRED(2805, HttpStatus.BAD_REQUEST, "Verify Code Expired, New Code Has Been Sent"),
    // Unauthenticated error (3xxx)
    UNAUTHENTICATED(3001, HttpStatus.UNAUTHORIZED, "Authentication Failed"),
    INVALID_TOKEN(3002, HttpStatus.UNAUTHORIZED, "Invalid Token"),
    ACCOUNT_NOT_VERIFIED(3003, HttpStatus.UNAUTHORIZED, "Account Not Verified"),
    PASSWORD_INCORRECT(3004, HttpStatus.UNAUTHORIZED, "Password Incorrect"),
    // Unauthorized error (4xxx)
    UNAUTHORIZED(4001, HttpStatus.FORBIDDEN, "Don't have permission"),
    // Not Found error (5xxx)
    USER_NOT_EXISTED(5001, HttpStatus.NOT_FOUND, "User Not Existed"),
    ROLE_NOT_EXISTED(5002, HttpStatus.NOT_FOUND, "Role Not Existed"),
    PERMISSION_NOT_EXISTED(5003, HttpStatus.NOT_FOUND, "Permission Not Existed"),
    ;

    final int code;
    final HttpStatus httpStatus;
    final String message;
}

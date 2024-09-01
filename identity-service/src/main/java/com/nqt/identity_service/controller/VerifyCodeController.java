package com.nqt.identity_service.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.*;

import com.nqt.identity_service.dto.request.SendVerificationRequest;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.exception.ErrorCode;
import com.nqt.identity_service.service.verifycode.VerifyCodeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerifyCodeController {

    VerifyCodeService verifyCodeService;

    @NonFinal
    @Value("${client.verification-success-page}")
    String clientVerificationSuccessPage;

    @NonFinal
    @Value("${client.verification-code-expired-page}")
    String clientVerificationCodeExpiredPage;

    @GetMapping
    public ApiResponse<Object> verifyCode(
            @RequestParam("code") String code, @RequestParam("user") String userId, HttpServletResponse response)
            throws MessagingException, IOException {
        if (verifyCodeService.verify(code, userId)) {
            response.sendRedirect(clientVerificationSuccessPage);
            return ApiResponse.builder().message("Verify success").build();
        }

        response.sendRedirect(clientVerificationCodeExpiredPage);
        return ApiResponse.builder()
                .code(ErrorCode.VERIFY_CODE_EXPIRED.getCode())
                .message(ErrorCode.VERIFY_CODE_EXPIRED.getMessage())
                .build();
    }

    @PostMapping("/send-verify-code")
    public ApiResponse<Object> sendVerifyCode(@RequestBody SendVerificationRequest request) {
        verifyCodeService.sendVerifyCode(request);

        return ApiResponse.builder()
                .message("Verification code has been sent to your email!")
                .build();
    }
}

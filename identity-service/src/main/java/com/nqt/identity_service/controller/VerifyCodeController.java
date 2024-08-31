package com.nqt.identity_service.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.service.verifycode.VerifyCodeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerifyCodeController {

    VerifyCodeService verifyCodeService;

    //    @NonFinal
    //    @Value("${client.verification-success-page}")
    //    String CLIENT_VERIFICATION_SUCCESS_PAGE;

    @GetMapping
    public ApiResponse<Object> verifyCode(
            @RequestParam("code") String code, @RequestParam("user") String userId, HttpServletResponse response)
            throws MessagingException {
        verifyCodeService.verify(code, userId);
        //        response.sendRedirect(CLIENT_VERIFICATION_SUCCESS_PAGE);
        return ApiResponse.builder().message("Verify success").build();
    }
}

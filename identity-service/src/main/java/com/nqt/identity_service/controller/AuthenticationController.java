package com.nqt.identity_service.controller;

import com.nqt.identity_service.dto.request.security.*;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.dto.response.AuthenticationResponse;
import com.nqt.identity_service.dto.response.IntrospectResponse;
import com.nqt.identity_service.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    @NonFinal
    @Value("${message.controller.authentication.change-password}")
    String changePasswordSuccessMessage;

    @NonFinal
    @Value("${message.controller.authentication.logout}")
    String logoutSuccessMessage;

    @NonFinal
    @Value("${code.success}")
    int codeSuccess;

    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ApiResponse<>(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest authenticationRequest) {
        return new ApiResponse<>(authenticationService.refreshToken(authenticationRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest authenticationRequest) {
        return new ApiResponse<>(authenticationService.introspect(authenticationRequest));
    }

    @PostMapping("/change-password")
    ApiResponse<Object> changePassword(@RequestBody @Valid ChangePasswordRequest authenticationRequest) {
        authenticationService.changePassword(authenticationRequest);

        return ApiResponse.builder()
                .code(codeSuccess)
                .message(changePasswordSuccessMessage)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Object> logout(@RequestBody LogoutRequest authenticationRequest) {
        authenticationService.logout(authenticationRequest);

        return ApiResponse.builder()
                .code(codeSuccess)
                .message(logoutSuccessMessage)
                .build();
    }

}
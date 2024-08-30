package com.nqt.identity_service.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.nqt.identity_service.dto.request.security.*;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.dto.response.AuthenticationResponse;
import com.nqt.identity_service.dto.response.IntrospectResponse;
import com.nqt.identity_service.service.authentication.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    @NonFinal
    @Value("${message.controller.authentication.change-password}")
    String changePasswordSuccessMessage;

    @NonFinal
    @Value("${message.controller.authentication.create-password}")
    String createPasswordSuccessMessage;

    @NonFinal
    @Value("${message.controller.authentication.logout}")
    String logoutSuccessMessage;

    @NonFinal
    @Value("${code.success}")
    int codeSuccess;

    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ApiResponse<>(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/outbound/authentication")
    public ApiResponse<AuthenticationResponse> outboundAuthentication(@RequestParam("code") String code) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.outboundAuthenticate(code))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest authenticationRequest) {
        return new ApiResponse<>(authenticationService.refreshToken(authenticationRequest));
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest authenticationRequest) {
        return new ApiResponse<>(authenticationService.introspect(authenticationRequest));
    }

    @PostMapping("/create-password")
    public ApiResponse<Object> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        authenticationService.createPassword(request);

        return ApiResponse.builder().message(createPasswordSuccessMessage).build();
    }

    @PostMapping("/change-password")
    public ApiResponse<Object> changePassword(@RequestBody @Valid ChangePasswordRequest authenticationRequest) {
        authenticationService.changePassword(authenticationRequest);

        return ApiResponse.builder().message(changePasswordSuccessMessage).build();
    }

    @PostMapping("/logout")
    public ApiResponse<Object> logout(@RequestBody LogoutRequest authenticationRequest) {
        authenticationService.logout(authenticationRequest);

        return ApiResponse.builder()
                .code(codeSuccess)
                .message(logoutSuccessMessage)
                .build();
    }
}

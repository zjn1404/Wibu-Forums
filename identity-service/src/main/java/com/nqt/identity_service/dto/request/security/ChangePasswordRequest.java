package com.nqt.identity_service.dto.request.security;

import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChangePasswordRequest {

    String oldPassword;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String newPassword;
}

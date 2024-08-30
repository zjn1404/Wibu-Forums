package com.nqt.identity_service.dto.request.security;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordCreationRequest {

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
}

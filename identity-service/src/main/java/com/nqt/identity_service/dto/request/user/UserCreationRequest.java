package com.nqt.identity_service.dto.request.user;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 6, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    String email;

    @Size(min = 10, max = 10, message = "PHONE_NUMBER_INVALID")
    String phoneNumber;

    String firstName;

    String lastName;

    Set<String> roles;

    Date dob;

    String address;
}

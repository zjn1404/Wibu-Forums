package com.nqt.identity_service.dto.response;

import java.util.Set;

import com.nqt.identity_service.entity.Role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String password;
    String email;
    String phoneNumber;
    Set<Role> roles;
}

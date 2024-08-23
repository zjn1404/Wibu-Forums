package com.nqt.identity_service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;
    Set<String> permissions;
}

package com.nqt.identity_service.dto.request;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleUpdateRequest {
    String description;
    Set<String> permissions;
}

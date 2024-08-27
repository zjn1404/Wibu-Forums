package com.nqt.identity_service.dto.response;

import java.util.Set;

import com.nqt.identity_service.entity.Permission;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    ;
    String description;
    Set<Permission> permissions;
}

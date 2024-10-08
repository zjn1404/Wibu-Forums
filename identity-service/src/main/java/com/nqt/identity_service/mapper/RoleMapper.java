package com.nqt.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.identity_service.dto.request.RoleRequest;
import com.nqt.identity_service.dto.request.RoleUpdateRequest;
import com.nqt.identity_service.dto.response.RoleResponse;
import com.nqt.identity_service.entity.Role;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}

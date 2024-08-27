package com.nqt.identity_service.service.role;

import java.util.List;

import com.nqt.identity_service.dto.request.RoleRequest;
import com.nqt.identity_service.dto.request.RoleUpdateRequest;
import com.nqt.identity_service.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse updateRole(String name, RoleUpdateRequest request);

    RoleResponse getRoleByName(String name);

    List<RoleResponse> getAllRoles();

    void deleteRoleByName(String name);
}

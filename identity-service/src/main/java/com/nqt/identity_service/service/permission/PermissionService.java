package com.nqt.identity_service.service.permission;

import java.util.List;

import com.nqt.identity_service.dto.request.PermissionRequest;
import com.nqt.identity_service.dto.response.PermissionResponse;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request);

    PermissionResponse getPermissionByName(String name);

    List<PermissionResponse> getAllPermissions();

    void deletePermissionByName(String name);
}

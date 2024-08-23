package com.nqt.identity_service.service.permission;

import com.nqt.identity_service.dto.request.PermissionRequest;
import com.nqt.identity_service.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request);

    PermissionResponse getPermissionByName(String name);

    List<PermissionResponse> getAllPermissions();

    void deletePermissionByName(String name);

}

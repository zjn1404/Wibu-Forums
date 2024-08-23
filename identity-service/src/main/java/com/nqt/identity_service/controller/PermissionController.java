package com.nqt.identity_service.controller;

import com.nqt.identity_service.dto.request.PermissionRequest;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.dto.response.PermissionResponse;
import com.nqt.identity_service.service.permission.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @NonFinal
    @Value("${message.controller.permission.delete}")
    String deleteSuccessMessage;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest permissionRequest) {
        return new ApiResponse<>(permissionService.createPermission(permissionRequest));
    }

    @GetMapping("/{name}")
    public ApiResponse<PermissionResponse> getPermission(@PathVariable("name") String name) {
        return new ApiResponse<>(permissionService.getPermissionByName(name));
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getPermissions() {
        return new ApiResponse<>(permissionService.getAllPermissions());
    }

    @DeleteMapping("/{name}")
    public ApiResponse<String> deletePermission(@PathVariable("name") String name) {
        permissionService.deletePermissionByName(name);

        return new ApiResponse<>(deleteSuccessMessage);
    }
}

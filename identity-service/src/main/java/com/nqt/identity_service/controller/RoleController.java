package com.nqt.identity_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nqt.identity_service.dto.request.RoleRequest;
import com.nqt.identity_service.dto.request.RoleUpdateRequest;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.dto.response.RoleResponse;
import com.nqt.identity_service.service.role.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @NonFinal
    @Value("${message.controller.role.delete}")
    String deleteSuccessMessage;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return new ApiResponse<>(roleService.createRole(roleRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{name}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable("name") String name, @RequestBody RoleUpdateRequest roleRequest) {
        return new ApiResponse<>(roleService.updateRole(name, roleRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{name}")
    public ApiResponse<RoleResponse> getRoleByName(@PathVariable("name") String name) {
        return new ApiResponse<>(roleService.getRoleByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return new ApiResponse<>(roleService.getAllRoles());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{name}")
    public ApiResponse<String> deleteRole(@PathVariable("name") String name) {
        roleService.deleteRoleByName(name);

        return new ApiResponse<>(deleteSuccessMessage);
    }
}

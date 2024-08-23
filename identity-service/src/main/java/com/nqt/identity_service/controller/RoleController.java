package com.nqt.identity_service.controller;

import com.nqt.identity_service.dto.request.RoleRequest;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.dto.response.RoleResponse;
import com.nqt.identity_service.service.role.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @NonFinal
    @Value("${message.controller.role.delete}")
    String deleteSuccessMessage;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return new ApiResponse<>(roleService.createRole(roleRequest));
    }

    @PutMapping
    public ApiResponse<RoleResponse> updateRole(@RequestBody RoleRequest roleRequest) {
        return new ApiResponse<>(roleService.updateRole(roleRequest));
    }

    @GetMapping("/{name}")
    public ApiResponse<RoleResponse> getRoleByName(@PathVariable("name") String name) {
        return new ApiResponse<>(roleService.getRoleByName(name));
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return new ApiResponse<>(roleService.getAllRoles());
    }

    @DeleteMapping("/{name}")
    public ApiResponse<String> deleteRole(@PathVariable("name") String name) {
        roleService.deleteRoleByName(name);

        return new ApiResponse<>(deleteSuccessMessage);
    }
}

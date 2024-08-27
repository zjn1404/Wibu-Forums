package com.nqt.identity_service.service.role;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nqt.identity_service.dto.request.RoleRequest;
import com.nqt.identity_service.dto.request.RoleUpdateRequest;
import com.nqt.identity_service.dto.response.RoleResponse;
import com.nqt.identity_service.entity.Permission;
import com.nqt.identity_service.entity.Role;
import com.nqt.identity_service.exception.AppException;
import com.nqt.identity_service.exception.ErrorCode;
import com.nqt.identity_service.mapper.RoleMapper;
import com.nqt.identity_service.repository.PermissionRepository;
import com.nqt.identity_service.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Role role = roleMapper.toRole(request);

        if (!Objects.isNull(request.getPermissions())) {
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
        }

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse updateRole(String name, RoleUpdateRequest request) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        roleMapper.updateRole(role, request);

        if (!Objects.isNull(request.getPermissions())) {
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
        }

        return roleMapper.toRoleResponse(roleRepository.saveAndFlush(role));
    }

    @Override
    public RoleResponse getRoleByName(String name) {
        return roleMapper.toRoleResponse(
                roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    // Only allow to delete when there's no user has this role
    @Override
    public void deleteRoleByName(String name) {
        roleRepository.deleteById(name);
    }
}

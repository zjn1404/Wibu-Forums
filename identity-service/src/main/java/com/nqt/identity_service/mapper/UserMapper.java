package com.nqt.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.identity_service.dto.request.user.UpdateMyInfoRequest;
import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.request.user.UserUpdateRequest;
import com.nqt.identity_service.dto.response.UserResponse;
import com.nqt.identity_service.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateMyInfo(@MappingTarget User user, UpdateMyInfoRequest request);

    @Mapping(target = "noPassword", ignore = true)
    UserResponse toUserResponse(User user);
}

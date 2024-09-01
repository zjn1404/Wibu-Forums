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
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    void updateMyInfo(@MappingTarget User user, UpdateMyInfoRequest request);

    UserResponse toUserResponse(User user);
}

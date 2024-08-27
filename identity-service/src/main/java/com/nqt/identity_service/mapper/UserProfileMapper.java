package com.nqt.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.request.user.UserUpdateRequest;
import com.nqt.identity_service.dto.request.userprofile.UserProfileCreationRequest;
import com.nqt.identity_service.dto.request.userprofile.UserProfileUpdateRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfileCreationRequest toUserProfileCreationRequest(UserCreationRequest request);

    UserProfileUpdateRequest toUserProfileUpdateRequest(UserUpdateRequest request);
}

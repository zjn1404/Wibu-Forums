package com.nqt.identity_service.mapper;

import com.nqt.identity_service.dto.request.user.UserUpdateRequest;
import com.nqt.identity_service.dto.request.userprofile.UserProfileCreationRequest;
import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.request.userprofile.UserProfileUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfileCreationRequest toUserProfileCreationRequest(UserCreationRequest request);
    UserProfileUpdateRequest toUserProfileUpdateRequest(UserUpdateRequest request);
}

package com.nqt.profile_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.profile_service.dto.request.UserProfileCreationRequest;
import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.entity.UserProfile;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileCreationRequest request);

    void updateUserProfile(@MappingTarget UserProfile userProfile, UserProfileUpdateRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}

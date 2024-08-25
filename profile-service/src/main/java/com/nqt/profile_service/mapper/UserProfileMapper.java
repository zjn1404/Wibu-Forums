package com.nqt.profile_service.mapper;

import com.nqt.profile_service.dto.request.UserProfileRequest;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileRequest request);

    void updateUserProfile(@MappingTarget UserProfile userProfile, UserProfileRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}

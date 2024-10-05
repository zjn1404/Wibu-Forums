package com.nqt.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.request.userprofile.UserProfileCreationRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    @Mapping(target = "userId", ignore = true)
    UserProfileCreationRequest toUserProfileCreationRequest(UserCreationRequest request);
}

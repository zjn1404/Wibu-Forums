package com.nqt.identity_service.mapper;

import com.nqt.identity_service.dto.request.UserProfileCreationRequest;
import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfileCreationRequest toUserProfileCreationRequest(UserCreationRequest request);
}

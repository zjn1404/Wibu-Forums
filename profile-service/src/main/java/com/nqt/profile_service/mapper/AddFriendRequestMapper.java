package com.nqt.profile_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.profile_service.dto.response.AddFriendRequestResponse;
import com.nqt.profile_service.entity.AddFriendRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddFriendRequestMapper {
    AddFriendRequestResponse toAddFriendRequestResponse(AddFriendRequest addFriendRequest);
}

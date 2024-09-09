package com.nqt.profile_service.service.userprofile;

import java.util.List;

import com.nqt.profile_service.dto.request.UserProfileCreationRequest;
import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.PageResponse;
import com.nqt.profile_service.dto.response.UserProfileResponse;

public interface UserProfileService {

    boolean addFriend(String friendId);

    UserProfileResponse createUserProfile(UserProfileCreationRequest request);

    UserProfileResponse updateUserProfile(String id, UserProfileUpdateRequest request);

    UserProfileResponse updateMyProfile(UserProfileUpdateRequest request);

    UserProfileResponse getMyProfile();

    UserProfileResponse getProfileByUserId(String userId);

    List<UserProfileResponse> getAllProfiles();

    PageResponse<UserProfileResponse> getAllFriends(int page, int size);

    boolean isFriendOf(String friendId);

    boolean unfriend(String friendId);
}

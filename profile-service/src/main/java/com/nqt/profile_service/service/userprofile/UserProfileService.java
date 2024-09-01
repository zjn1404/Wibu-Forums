package com.nqt.profile_service.service.userprofile;

import java.util.List;

import com.nqt.profile_service.dto.request.UserProfileCreationRequest;
import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse createUserProfile(UserProfileCreationRequest request);

    UserProfileResponse updateUserProfile(String id, UserProfileUpdateRequest request);

    UserProfileResponse updateMyProfile(UserProfileUpdateRequest request);

    UserProfileResponse getMyProfile();

    List<UserProfileResponse> getAllProfiles();
}

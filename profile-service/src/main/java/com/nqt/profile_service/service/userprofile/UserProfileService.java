package com.nqt.profile_service.service.userprofile;

import com.nqt.profile_service.dto.request.UserProfileCreationRequest;
import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse createUserProfile(UserProfileCreationRequest request);

    UserProfileResponse updateUserProfile(String id, UserProfileUpdateRequest request);

    UserProfileResponse updateUserProfileByUserId(String userId, UserProfileUpdateRequest request);

    UserProfileResponse getUserProfileById(String userId);

}

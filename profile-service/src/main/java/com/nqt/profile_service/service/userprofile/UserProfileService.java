package com.nqt.profile_service.service.userprofile;

import com.nqt.profile_service.dto.request.UserProfileRequest;
import com.nqt.profile_service.dto.response.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse createUserProfile(UserProfileRequest request);

    UserProfileResponse updateUserProfile(String id, UserProfileRequest request);

    UserProfileResponse getUserProfileById(String userId);
}

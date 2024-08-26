package com.nqt.profile_service.service.userprofile;

import com.nqt.profile_service.dto.request.UserProfileCreationRequest;
import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.entity.UserProfile;
import com.nqt.profile_service.exception.AppException;
import com.nqt.profile_service.exception.ErrorCode;
import com.nqt.profile_service.mapper.UserProfileMapper;
import com.nqt.profile_service.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileServiceImp implements UserProfileService{

    UserProfileRepository userProfileRepository;

    UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse createUserProfile(UserProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @Override
    public UserProfileResponse updateUserProfile(String id, UserProfileUpdateRequest request) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        userProfileMapper.updateUserProfile(userProfile, request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @Override
    public UserProfileResponse updateUserProfileByUserId(String userId, UserProfileUpdateRequest request) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        userProfileMapper.updateUserProfile(userProfile, request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @Override
    public UserProfileResponse getUserProfileById(String id) {
        return userProfileMapper.toUserProfileResponse(userProfileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND)));
    }
}

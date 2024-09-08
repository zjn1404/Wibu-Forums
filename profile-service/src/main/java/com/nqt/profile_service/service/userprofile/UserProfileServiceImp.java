package com.nqt.profile_service.service.userprofile;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nqt.profile_service.dto.request.UserProfileCreationRequest;
import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.PageResponse;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.entity.UserProfile;
import com.nqt.profile_service.exception.AppException;
import com.nqt.profile_service.exception.ErrorCode;
import com.nqt.profile_service.mapper.UserProfileMapper;
import com.nqt.profile_service.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileServiceImp implements UserProfileService {

    UserProfileRepository userProfileRepository;

    UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse createUserProfile(UserProfileCreationRequest request) {
        if (userProfileRepository.existsByUserId(request.getUserId())) {
            throw new AppException(ErrorCode.USER_PROFILE_EXISTED);
        }

        UserProfile userProfile = userProfileMapper.toUserProfile(request);

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @Override
    public UserProfileResponse updateUserProfile(String id, UserProfileUpdateRequest request) {
        UserProfile userProfile = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        userProfileMapper.updateUserProfile(userProfile, request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @Override
    public UserProfileResponse updateMyProfile(UserProfileUpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserProfile userProfile = userProfileRepository
                .findByUserId(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        userProfileMapper.updateUserProfile(userProfile, request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @Override
    public UserProfileResponse getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userProfileMapper.toUserProfileResponse(userProfileRepository
                .findByUserId(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND)));
    }

    @Override
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    @Override
    public PageResponse<UserProfileResponse> getAllFriends(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Page<UserProfile> userProfiles = userProfileRepository.findAllFriends(authentication.getName(), pageable);

        return PageResponse.<UserProfileResponse>builder()
                .currentPage(page)
                .totalElements(userProfiles.getTotalElements())
                .totalPages(userProfiles.getTotalPages())
                .pageSize(userProfiles.getSize())
                .data(userProfiles
                        .get()
                        .map(userProfileMapper::toUserProfileResponse)
                        .toList())
                .build();
    }

    @Override
    public boolean addFriend(String friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userProfileRepository.addFriend(authentication.getName(), friendId);
    }

    @Override
    public boolean unfriend(String friendId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !userProfileRepository.unfriend(authentication.getName(), friendId);
    }
}

package com.nqt.profile_service.controller;

import com.nqt.profile_service.dto.request.UserProfileCreationRequest;
import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.ApiResponse;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.service.userprofile.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {

    UserProfileService userProfileService;

    @PostMapping
    public ApiResponse<UserProfileResponse> createUserProfile(@RequestBody UserProfileCreationRequest request) {
        return new ApiResponse<>(userProfileService.createUserProfile(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserProfileResponse> updateUserProfile(@PathVariable String id, @RequestBody UserProfileUpdateRequest request) {
        return new ApiResponse<>(userProfileService.updateUserProfile(id, request));
    }

    @PutMapping
    public ApiResponse<UserProfileResponse> deleteUserProfile(@RequestParam("userId") String userId, @RequestBody UserProfileUpdateRequest request) {
        return new ApiResponse<>(userProfileService.updateUserProfileByUserId(userId, request));
    }
}
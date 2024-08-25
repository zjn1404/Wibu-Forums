package com.nqt.profile_service.controller;

import com.nqt.profile_service.dto.request.UserProfileRequest;
import com.nqt.profile_service.dto.response.ApiResponse;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.service.userprofile.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    UserProfileService userProfileService;

    @PostMapping
    public ApiResponse<UserProfileResponse> createUserProfile(@RequestBody UserProfileRequest request) {
        return new ApiResponse<>(userProfileService.createUserProfile(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserProfileResponse> updateUserProfile(@PathVariable String id, @RequestBody UserProfileRequest request) {
        return new ApiResponse<>(userProfileService.updateUserProfile(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserProfileResponse> getUserProfile(@PathVariable String id) {
        return new ApiResponse<>(userProfileService.getUserProfileById(id));
    }
}

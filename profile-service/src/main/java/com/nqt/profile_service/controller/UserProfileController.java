package com.nqt.profile_service.controller;

import com.nqt.profile_service.dto.response.ApiResponse;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.service.userprofile.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ApiResponse<UserProfileResponse> getUserProfile(@PathVariable String id) {
        return new ApiResponse<>(userProfileService.getUserProfileById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllProfiles())
                .build();
    }
}

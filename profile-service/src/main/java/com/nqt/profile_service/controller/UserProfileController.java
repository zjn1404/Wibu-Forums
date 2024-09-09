package com.nqt.profile_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nqt.profile_service.dto.request.UserProfileUpdateRequest;
import com.nqt.profile_service.dto.response.ApiResponse;
import com.nqt.profile_service.dto.response.PageResponse;
import com.nqt.profile_service.dto.response.UserProfileResponse;
import com.nqt.profile_service.service.userprofile.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    @NonFinal
    @Value("${message.controller.user-profile.add-friend-success}")
    String addFriendSuccessMessage;

    @NonFinal
    @Value("${message.controller.user-profile.add-friend-fail}")
    String addFriendFailMessage;

    @NonFinal
    @Value("${message.controller.user-profile.unfriend-success}")
    String unfriendSuccessMessage;

    @NonFinal
    @Value("${message.controller.user-profile.unfriend-fail}")
    String unfriendFailMessage;

    UserProfileService userProfileService;

    @PostMapping("/add-friend")
    public ApiResponse<Void> addFriend(@RequestParam("friendId") String friendId) {
        boolean isFriendAdded = userProfileService.addFriend(friendId);
        String response = isFriendAdded ? addFriendSuccessMessage : addFriendFailMessage;

        return ApiResponse.<Void>builder().message(response).build();
    }

    @GetMapping("/my-profile")
    public ApiResponse<UserProfileResponse> getMyProfile() {
        return new ApiResponse<>(userProfileService.getMyProfile());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllProfiles())
                .build();
    }

    @GetMapping("/get-friends")
    public ApiResponse<PageResponse<UserProfileResponse>> getFriends(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UserProfileResponse>>builder()
                .result(userProfileService.getAllFriends(page, size))
                .build();
    }

    @GetMapping("get-by-user-id")
    public ApiResponse<UserProfileResponse> getUserProfileByUserId(@RequestParam("userId") String userId) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfileByUserId(userId))
                .build();
    }

    @GetMapping("/is-friend")
    public ApiResponse<Boolean> isFriend(@RequestParam("friendId") String friendId) {
        return ApiResponse.<Boolean>builder()
                .result(userProfileService.isFriendOf(friendId))
                .build();
    }

    @PutMapping
    public ApiResponse<UserProfileResponse> updateMyProfile(@RequestBody UserProfileUpdateRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateMyProfile(request))
                .build();
    }

    @DeleteMapping("/unfriend")
    public ApiResponse<Void> unfriend(@RequestParam("friendId") String friendId) {
        boolean hasUnfriended = userProfileService.unfriend(friendId);
        String response = hasUnfriended ? unfriendSuccessMessage : unfriendFailMessage;

        return ApiResponse.<Void>builder().message(response).build();
    }
}

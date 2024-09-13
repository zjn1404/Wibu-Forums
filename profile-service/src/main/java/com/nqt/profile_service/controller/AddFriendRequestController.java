package com.nqt.profile_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.nqt.profile_service.dto.request.ResponseAddFriendRequest;
import com.nqt.profile_service.dto.response.AddFriendRequestResponse;
import com.nqt.profile_service.dto.response.ApiResponse;
import com.nqt.profile_service.dto.response.PageResponse;
import com.nqt.profile_service.service.addfriendrequest.AddFriendRequestService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddFriendRequestController {

    AddFriendRequestService addFriendRequestService;

    @NonFinal
    @Value("${message.controller.user-profile.response-add-friend-success}")
    String responseAddFriendSuccessMessage;

    @NonFinal
    @Value("${message.controller.user-profile.add-friend-success}")
    String addFriendSuccessMessage;

    @PostMapping("/add-friend")
    public ApiResponse<Void> sendAddFriendRequest(@RequestParam("friendId") String friendId) {
        addFriendRequestService.sendAddFriendRequest(friendId);
        return ApiResponse.<Void>builder().message(addFriendSuccessMessage).build();
    }

    @PostMapping("/add-friend-response")
    public ApiResponse<Void> updateAddFriendRequest(@RequestBody ResponseAddFriendRequest request) {
        addFriendRequestService.responseAddFriendRequest(request);
        return ApiResponse.<Void>builder()
                .message(responseAddFriendSuccessMessage)
                .build();
    }

    @GetMapping("/all-add-friend")
    public ApiResponse<PageResponse<AddFriendRequestResponse>> getAllAddFriendRequestsByUserId(
            @RequestParam("userId") String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        PageResponse<AddFriendRequestResponse> responses =
                addFriendRequestService.getAllAddFriendRequestsByUserId(userId, page, size);

        return ApiResponse.<PageResponse<AddFriendRequestResponse>>builder()
                .result(responses)
                .build();
    }
}

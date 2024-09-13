package com.nqt.profile_service.service.addfriendrequest;

import com.nqt.profile_service.dto.request.ResponseAddFriendRequest;
import com.nqt.profile_service.dto.response.AddFriendRequestResponse;
import com.nqt.profile_service.dto.response.PageResponse;

public interface AddFriendRequestService {
    void sendAddFriendRequest(String friendId);

    void responseAddFriendRequest(ResponseAddFriendRequest request);

    PageResponse<AddFriendRequestResponse> getAllAddFriendRequestsByUserId(String userId, int page, int size);
}

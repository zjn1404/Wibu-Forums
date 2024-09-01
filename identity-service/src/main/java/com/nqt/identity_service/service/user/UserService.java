package com.nqt.identity_service.service.user;

import java.util.List;

import com.nqt.identity_service.dto.request.user.UpdateMyInfoRequest;
import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.request.user.UserUpdateRequest;
import com.nqt.identity_service.dto.response.UserResponse;
import com.nqt.identity_service.entity.User;

public interface UserService {

    UserResponse createUser(UserCreationRequest request);

    User createInternalUser(UserCreationRequest request);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    UserResponse updateMyInfo(UpdateMyInfoRequest request);

    UserResponse getUserById(String userId);

    UserResponse getMyInfo();

    List<UserResponse> getAllUsers();

    void deleteUserById(String userId);
}

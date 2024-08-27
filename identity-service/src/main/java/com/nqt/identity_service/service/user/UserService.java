package com.nqt.identity_service.service.user;

import java.util.List;

import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.request.user.UserUpdateRequest;
import com.nqt.identity_service.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    UserResponse getUserById(String userId);

    UserResponse getMyInfo();

    List<UserResponse> getAllUsers();

    void deleteUserById(String userId);
}

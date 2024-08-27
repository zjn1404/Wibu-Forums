package com.nqt.identity_service.repository.profileservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.nqt.identity_service.configuration.AuthenticationRequestInterceptor;
import com.nqt.identity_service.dto.request.userprofile.UserProfileCreationRequest;
import com.nqt.identity_service.dto.request.userprofile.UserProfileUpdateRequest;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.dto.response.UserProfileResponse;

@FeignClient(
        name = "profile-service",
        url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> createUserProfile(@RequestBody UserProfileCreationRequest request);

    @PutMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> updateUserProfile(
            @RequestParam("userId") String userId, @RequestBody UserProfileUpdateRequest request);
}

package com.nqt.identity_service.repository.profileservice;

import com.nqt.identity_service.dto.request.UserProfileCreationRequest;
import com.nqt.identity_service.dto.response.ApiResponse;
import com.nqt.identity_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> createUserProfile(@RequestBody UserProfileCreationRequest request);

}

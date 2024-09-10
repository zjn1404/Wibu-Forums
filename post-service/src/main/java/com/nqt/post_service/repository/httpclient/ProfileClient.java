package com.nqt.post_service.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nqt.post_service.configuration.AuthenticationRequestInterceptor;
import com.nqt.post_service.dto.response.ApiResponse;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.dto.response.UserProfileResponse;

import java.util.List;

@FeignClient(
        name = "profile-service",
        url = "${app.services.profile.url}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping(value = "/users/get-friends", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<PageResponse<UserProfileResponse>> getFriends(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size);

    @GetMapping(value = "/users/get-by-user-id", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> getByUserId(@RequestParam("userId") String userId);

    @GetMapping(value = "/users/get-all-friends", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<UserProfileResponse>> getAllFriends();
}

package com.nqt.post_service.controller;

import com.nqt.post_service.dto.request.PostRequest;
import com.nqt.post_service.dto.response.ApiResponse;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.service.post.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;

    @PostMapping("/create-post")
    public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping("/my-posts")
    public ApiResponse<PageResponse<PostResponse>> getMyPosts(@RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") int pageSize) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getMyPosts(currentPage, pageSize))
                .build();
    }

}

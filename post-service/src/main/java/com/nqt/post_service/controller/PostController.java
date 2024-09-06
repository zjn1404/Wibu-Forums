package com.nqt.post_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.nqt.post_service.dto.request.post.PostRequest;
import com.nqt.post_service.dto.request.post.PostUpdateRequest;
import com.nqt.post_service.dto.response.ApiResponse;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.service.post.PostService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;

    @NonFinal
    @Value("${message.controller.post.delete}")
    String deletePostSuccessMessage;

    @NonFinal
    @Value("${message.controller.post.update}")
    String updatePostSuccessMessage;

    @PostMapping("/create-post")
    public ApiResponse<PostResponse> createPost(@ModelAttribute PostRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping("/my-posts")
    public ApiResponse<PageResponse<PostResponse>> getMyPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int currentPage,
            @RequestParam(value = "size", required = false, defaultValue = "10") int pageSize) {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getMyPosts(currentPage, pageSize))
                .build();
    }

    @PutMapping("/update-post/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable("postId") String postId, @RequestBody PostUpdateRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(postId, request))
                .message(updatePostSuccessMessage)
                .build();
    }

    @DeleteMapping("/delete-post")
    public ApiResponse<Void> deletePost(@RequestParam("postId") String postId) {
        postService.deletePost(postId);

        return ApiResponse.<Void>builder().message(deletePostSuccessMessage).build();
    }
}

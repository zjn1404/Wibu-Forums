package com.nqt.post_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.nqt.post_service.dto.request.comment.CommentCreationRequest;
import com.nqt.post_service.dto.request.comment.CommentUpdateRequest;
import com.nqt.post_service.dto.response.ApiResponse;
import com.nqt.post_service.dto.response.CommentResponse;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.service.comment.CommentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    @NonFinal
    @Value("${message.controller.comment.delete}")
    String deleteCommentSuccessMessage;

    CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponse> createComment(@RequestBody CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<CommentResponse>> getCommentsOfPost(
            @RequestParam("postId") String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CommentResponse>>builder()
                .result(commentService.getCommentsOfPost(postId, page, size))
                .build();
    }

    @PutMapping
    public ApiResponse<CommentResponse> updateComment(
            @RequestParam("commentId") String commentId, @RequestBody CommentUpdateRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(commentId, request))
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteComment(@RequestParam("commentId") String commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder().message(deleteCommentSuccessMessage).build();
    }
}

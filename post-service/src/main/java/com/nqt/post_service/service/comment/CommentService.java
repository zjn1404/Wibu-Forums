package com.nqt.post_service.service.comment;

import com.nqt.post_service.dto.request.comment.CommentCreationRequest;
import com.nqt.post_service.dto.request.comment.CommentUpdateRequest;
import com.nqt.post_service.dto.response.CommentResponse;
import com.nqt.post_service.dto.response.PageResponse;

public interface CommentService {

    CommentResponse createComment(CommentCreationRequest request);

    PageResponse<CommentResponse> getCommentsOfPost(String postId, int page, int size);

    CommentResponse updateComment(String commentId, CommentUpdateRequest request);

    void deleteComment(String commentId);
}

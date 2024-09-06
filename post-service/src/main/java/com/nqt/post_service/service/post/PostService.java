package com.nqt.post_service.service.post;

import com.nqt.post_service.dto.request.PostRequest;
import com.nqt.post_service.dto.request.PostUpdateRequest;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.dto.response.PostResponse;

public interface PostService {
    PostResponse createPost(PostRequest request);

    PageResponse<PostResponse> getMyPosts(int currentPage, int pageSize);

    PostResponse updatePost(String postId, PostUpdateRequest request);

    void deletePost(String postId);
}

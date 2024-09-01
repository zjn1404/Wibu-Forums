package com.nqt.post_service.service.post;

import com.nqt.post_service.dto.request.PostRequest;
import com.nqt.post_service.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest postRequest);

    List<PostResponse> getMyPosts();
}

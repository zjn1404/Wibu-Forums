package com.nqt.post_service.service.post;

import com.nqt.post_service.dto.request.PostRequest;
import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.entity.Post;
import com.nqt.post_service.mapper.PostMapper;
import com.nqt.post_service.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImp implements PostService{
    PostRepository postRepository;

    PostMapper mapper;

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Post post = Post.builder()
                .userId(authentication.getName())
                .content(postRequest.getContent())
                .postedDate(new Date())
                .modifiedDate(new Date())
                .build();

        return mapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public List<PostResponse> getMyPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return postRepository.findAllByUserId(authentication.getName()).stream().map(mapper::toPostResponse).toList();
    }
}

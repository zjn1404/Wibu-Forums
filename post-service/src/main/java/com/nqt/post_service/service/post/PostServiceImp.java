package com.nqt.post_service.service.post;

import com.nqt.post_service.dto.request.PostRequest;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.entity.Post;
import com.nqt.post_service.mapper.PostMapper;
import com.nqt.post_service.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public PageResponse<PostResponse> getMyPosts(int currentPage, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort = Sort.by("postedDate");
        // index-base 1
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Post> posts = postRepository.findAllByUserId(authentication.getName(), pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(posts.get().map(mapper::toPostResponse).toList())
                .build();
    }
}

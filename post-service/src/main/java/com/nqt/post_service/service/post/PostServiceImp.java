package com.nqt.post_service.service.post;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nqt.post_service.dto.request.PostRequest;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.entity.Post;
import com.nqt.post_service.exception.AppException;
import com.nqt.post_service.exception.ErrorCode;
import com.nqt.post_service.mapper.PostMapper;
import com.nqt.post_service.repository.PostRepository;
import com.nqt.post_service.utils.formatter.DateFormatter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImp implements PostService {
    PostRepository postRepository;

    PostMapper postMapper;

    DateFormatter dateFormatter;

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        log.info(postRequest.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Binary image = null;
        if (postRequest.getImage() != null) {
            try {
                image = new Binary(
                        BsonBinarySubType.BINARY, postRequest.getImage().getBytes());
            } catch (IOException e) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }
        }

        Post post = Post.builder()
                .userId(authentication.getName())
                .content(postRequest.getContent())
                .image(image)
                .postedDate(new Date())
                .modifiedDate(new Date())
                .build();

        PostResponse postResponse = postMapper.toPostResponse(postRepository.save(post));
        postResponse.setImage(Base64.getEncoder().encodeToString(post.getImage().getData()));

        return postResponse;
    }

    @Override
    public PageResponse<PostResponse> getMyPosts(int currentPage, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort = Sort.by(Sort.Direction.DESC, "postedDate");
        // index-base 1
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Post> posts = postRepository.findAllByUserId(authentication.getName(), pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(post -> {
                    PostResponse postResponse = postMapper.toPostResponse(post);
                    postResponse.setFormattedPostedDate(dateFormatter.format(post.getPostedDate()));
                    if (post.getImage() != null) {
                        postResponse.setImage(Base64.getEncoder()
                                .encodeToString(post.getImage().getData()));
                    }
                    return postResponse;
                })
                .toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(postResponses)
                .build();
    }
}

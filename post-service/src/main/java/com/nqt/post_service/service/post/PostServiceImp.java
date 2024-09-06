package com.nqt.post_service.service.post;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import com.nqt.post_service.dto.request.post.PostRequest;
import com.nqt.post_service.dto.request.post.PostUpdateRequest;
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
    public PostResponse createPost(PostRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Binary> images = null;
        if (request.getImages() != null) {
            images = new ArrayList<>();
            try {
                for (MultipartFile img : request.getImages()) {
                    images.add(new Binary(BsonBinarySubType.BINARY, img.getBytes()));
                }
            } catch (IOException e) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }
        }

        Post post = Post.builder()
                .userId(authentication.getName())
                .content(request.getContent())
                .images(images)
                .postedDate(new Date())
                .modifiedDate(new Date())
                .build();

        PostResponse postResponse = postMapper.toPostResponse(postRepository.save(post));
        postResponse.setImages(encodeImages(post.getImages()));

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
                    if (post.getImages() != null) {
                        postResponse.setImages(encodeImages(post.getImages()));
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

    @Override
    public PostResponse updatePost(String postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        postMapper.updatePost(post, request);
        post.setModifiedDate(new Date());

        postRepository.save(post);

        PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setImages(encodeImages(post.getImages()));
        postResponse.setFormattedPostedDate(dateFormatter.format(post.getPostedDate()));

        return postResponse;
    }

    @Override
    public void deletePost(String postId) {
        postRepository.deleteById(postId);
    }

    private List<String> encodeImages(List<Binary> images) {
        if (images == null) {
            return List.of();
        }

        List<String> encodedImages = new ArrayList<>();
        for (Binary image : images) {
            encodedImages.add(Base64.getEncoder().encodeToString(image.getData()));
        }
        return encodedImages;
    }
}

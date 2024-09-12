package com.nqt.post_service.service.post;

import java.io.IOException;
import java.util.*;

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

import com.nqt.event.dto.Recipient;
import com.nqt.event.notification.NotificationType;
import com.nqt.post_service.dto.request.post.PostRequest;
import com.nqt.post_service.dto.request.post.PostUpdateRequest;
import com.nqt.post_service.dto.response.ApiResponse;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.dto.response.UserProfileResponse;
import com.nqt.post_service.entity.Post;
import com.nqt.post_service.exception.AppException;
import com.nqt.post_service.exception.ErrorCode;
import com.nqt.post_service.mapper.PostMapper;
import com.nqt.post_service.repository.CommentRepository;
import com.nqt.post_service.repository.PostRepository;
import com.nqt.post_service.repository.httpclient.ProfileClient;
import com.nqt.post_service.service.kafka.KafkaProduceService;
import com.nqt.post_service.utils.formatter.DateFormatter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImp implements PostService {

    @NonFinal
    String defaultSortProperty = "postedDate";

    PostRepository postRepository;
    CommentRepository commentRepository;

    ProfileClient profileClient;

    PostMapper postMapper;

    DateFormatter dateFormatter;

    KafkaProduceService kafkaProduceService;

    @Override
    public PostResponse createPost(PostRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserProfileResponse userProfile =
                profileClient.getByUserId(authentication.getName()).getResult();

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

        List<Recipient> recipients = profileClient.getAllFriends().getResult().stream()
                .map(profile -> Recipient.builder()
                        .userId(profile.getUserId())
                        .name(String.format("%s %s", profile.getFirstName(), profile.getLastName()))
                        .build())
                .toList();

        String body = NotificationType.CREATE_POST.getBody() + userProfile.getFirstName() + userProfile.getLastName();
        kafkaProduceService.sendNotification(NotificationType.CREATE_POST, recipients, post.getId(), body);

        return postResponse;
    }

    @Override
    public PageResponse<PostResponse> getPostById(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setImages(encodeImages(post.getImages()));
        postResponse.setFormattedPostedDate(dateFormatter.format(post.getPostedDate()));

        return PageResponse.<PostResponse>builder()
                .currentPage(1)
                .totalPages(1)
                .totalElements(1)
                .pageSize(1)
                .data(List.of(postResponse))
                .build();
    }

    @Override
    public PageResponse<PostResponse> getMyPosts(int currentPage, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort = Sort.by(Sort.Direction.DESC, defaultSortProperty);
        // index-base 1
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Post> posts = postRepository.findAllByUserId(authentication.getName(), pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(toPostResponses(posts))
                .build();
    }

    @Override
    public PageResponse<PostResponse> getPostByUserId(String userId, int currentPage, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, defaultSortProperty);
        // index-base 1
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Post> posts = postRepository.findAllByUserId(userId, pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(toPostResponses(posts))
                .build();
    }

    @Override
    public PageResponse<PostResponse> getFriendPosts(int currentPage, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, defaultSortProperty);
        // index-base 1
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);
        ApiResponse<PageResponse<UserProfileResponse>> friends = profileClient.getFriends(currentPage, pageSize);
        List<String> friendIds = friends.getResult().getData().stream()
                .map(UserProfileResponse::getUserId)
                .toList();

        Page<Post> posts = postRepository.findAllByUserIdIn(friendIds, pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .data(toPostResponses(posts))
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
        commentRepository.deleteAllByPostId(postId);
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

    private List<PostResponse> toPostResponses(Page<Post> posts) {
        return posts.getContent().stream()
                .map(post -> {
                    PostResponse postResponse = postMapper.toPostResponse(post);
                    postResponse.setFormattedPostedDate(dateFormatter.format(post.getPostedDate()));
                    if (post.getImages() != null) {
                        postResponse.setImages(encodeImages(post.getImages()));
                    }
                    return postResponse;
                })
                .toList();
    }
}

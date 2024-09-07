package com.nqt.post_service.service.comment;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nqt.post_service.dto.request.comment.CommentCreationRequest;
import com.nqt.post_service.dto.request.comment.CommentUpdateRequest;
import com.nqt.post_service.dto.response.CommentResponse;
import com.nqt.post_service.dto.response.PageResponse;
import com.nqt.post_service.entity.Comment;
import com.nqt.post_service.exception.AppException;
import com.nqt.post_service.exception.ErrorCode;
import com.nqt.post_service.mapper.CommentMapper;
import com.nqt.post_service.repository.CommentRepository;
import com.nqt.post_service.repository.PostRepository;
import com.nqt.post_service.utils.formatter.DateFormatter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImp implements CommentService {

    CommentRepository commentRepository;
    PostRepository postRepository;

    CommentMapper commentMapper;

    DateFormatter dateFormatter;

    @Override
    public CommentResponse createComment(CommentCreationRequest request) {
        if (!postRepository.existsById(request.getPostId())) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Comment comment = Comment.builder()
                .userId(authentication.getName())
                .content(request.getContent())
                .postId(request.getPostId())
                .postedDate(new Date())
                .modifiedDate(new Date())
                .build();

        commentRepository.save(comment);

        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setFormattedPostedDate(dateFormatter.format(commentResponse.getModifiedDate()));

        return commentResponse;
    }

    @Override
    public PageResponse<CommentResponse> getCommentsOfPost(String postId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
                    commentResponse.setFormattedPostedDate(dateFormatter.format(comment.getPostedDate()));
                    return commentResponse;
                })
                .toList();

        return PageResponse.<CommentResponse>builder()
                .currentPage(page)
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .data(commentResponses)
                .build();
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentUpdateRequest request) {
        Comment comment =
                commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        commentMapper.updateComment(comment, request);
        comment.setModifiedDate(new Date());
        commentRepository.save(comment);

        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setFormattedPostedDate(dateFormatter.format(commentResponse.getModifiedDate()));

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}

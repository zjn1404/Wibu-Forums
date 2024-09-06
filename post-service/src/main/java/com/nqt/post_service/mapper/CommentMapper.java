package com.nqt.post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.post_service.dto.request.comment.CommentUpdateRequest;
import com.nqt.post_service.dto.response.CommentResponse;
import com.nqt.post_service.entity.Comment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    void updateComment(@MappingTarget Comment comment, CommentUpdateRequest request);

    CommentResponse toCommentResponse(Comment comment);
}

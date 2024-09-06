package com.nqt.post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.post_service.dto.request.post.PostUpdateRequest;
import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.entity.Post;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    @Mapping(target = "images", ignore = true)
    PostResponse toPostResponse(Post post);

    void updatePost(@MappingTarget Post post, PostUpdateRequest request);
}

package com.nqt.post_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.entity.Post;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    @Mapping(target = "image", ignore = true)
    PostResponse toPostResponse(Post post);
}

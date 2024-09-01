package com.nqt.post_service.mapper;

import com.nqt.post_service.dto.response.PostResponse;
import com.nqt.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}

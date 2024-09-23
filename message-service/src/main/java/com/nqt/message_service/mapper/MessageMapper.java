package com.nqt.message_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nqt.message_service.dto.response.MessageResponse;
import com.nqt.message_service.entity.Message;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    MessageResponse toMessageResponse(Message message);
}

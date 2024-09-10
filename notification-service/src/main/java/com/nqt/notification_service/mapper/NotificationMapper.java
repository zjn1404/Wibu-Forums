package com.nqt.notification_service.mapper;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.notification_service.dto.response.NotificationResponse;
import com.nqt.notification_service.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {

    @Mapping(target = "recipientsReadStatus", ignore = true)
    Notification toNotification(NotificationEvent event);

    @Mapping(target = "formatedCreatedDate", ignore = true)
    NotificationResponse toNotificationResponse(Notification notification);
}

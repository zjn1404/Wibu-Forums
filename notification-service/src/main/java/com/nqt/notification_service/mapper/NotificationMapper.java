package com.nqt.notification_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.notification_service.dto.response.NotificationResponse;
import com.nqt.notification_service.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recipientsReadStatus", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Notification toNotification(NotificationEvent event);

    @Mapping(target = "formatedCreatedDate", ignore = true)
    NotificationResponse toNotificationResponse(Notification notification);
}

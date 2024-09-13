package com.nqt.notification_service.service.notification;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.notification_service.dto.request.MarkNotificationAsReadRequest;
import com.nqt.notification_service.dto.response.NotificationResponse;
import com.nqt.notification_service.dto.response.PageResponse;

public interface NotificationService {
    void processNotification(NotificationEvent notificationEvent);

    void markNotificationAsRead(MarkNotificationAsReadRequest request);

    PageResponse<NotificationResponse> findAllUnreadNotifications(int page, int size);
}

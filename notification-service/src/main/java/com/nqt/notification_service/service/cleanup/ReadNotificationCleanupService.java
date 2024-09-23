package com.nqt.notification_service.service.cleanup;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nqt.notification_service.repository.NotificationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReadNotificationCleanupService {

    NotificationRepository notificationRepository;

    @Scheduled(fixedDelay = 86400000)
    public void cleanup() {
        notificationRepository.deleteAllReadNotifications();
    }
}

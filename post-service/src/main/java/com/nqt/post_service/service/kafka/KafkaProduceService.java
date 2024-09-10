package com.nqt.post_service.service.kafka;

import com.nqt.event.dto.Recipient;
import com.nqt.event.notification.NotificationType;

import java.util.List;

public interface KafkaProduceService {
    void sendNotification(NotificationType notificationType, List<Recipient> recipients, String body);
}

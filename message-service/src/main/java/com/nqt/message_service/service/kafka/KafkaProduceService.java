package com.nqt.message_service.service.kafka;

import java.util.List;

import com.nqt.event.dto.Recipient;
import com.nqt.event.notification.NotificationType;

public interface KafkaProduceService {
    void sendNotification(
            NotificationType notificationType, List<Recipient> recipients, String componentId, String body);
}

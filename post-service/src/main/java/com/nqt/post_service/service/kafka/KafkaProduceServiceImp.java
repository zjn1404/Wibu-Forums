package com.nqt.post_service.service.kafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.event.dto.Recipient;
import com.nqt.event.notification.NotificationType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProduceServiceImp implements KafkaProduceService {

    @NonFinal
    @Value("${app.kafka.topics.notification}")
    String notificationTopic;

    KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public void sendNotification(
            NotificationType notificationType, List<Recipient> recipients, String componentId, String body) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel(notificationType.getChannel())
                .subject(notificationType.getSubject())
                .recipients(recipients)
                .componentId(componentId)
                .body(body)
                .build();

        kafkaTemplate.send(notificationTopic, notificationEvent);
    }
}

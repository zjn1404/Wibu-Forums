package com.nqt.post_service.service.kafka;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.event.dto.Recipient;
import com.nqt.event.notification.NotificationType;
import com.nqt.post_service.dto.response.UserProfileResponse;
import com.nqt.post_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProduceServiceImp implements KafkaProduceService {

    @NonFinal
    @Value("${app.kafka.topics.notification}")
    String notificationTopic;

    KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    ProfileClient profileClient;

    @Override
    public void sendNotification(NotificationType notificationType, List<Recipient> recipients, String body) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel(notificationType.getChannel())
                .subject(notificationType.getSubject())
                .recipients(recipients)
                .body(body)
                .build();

        kafkaTemplate.send(notificationTopic, notificationEvent);
    }
}

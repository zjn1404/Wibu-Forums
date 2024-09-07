package com.nqt.notification_service.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.notification_service.dto.request.SendMailRequest;
import com.nqt.notification_service.service.MailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    MailService mailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotification(NotificationEvent message) {

        mailService.sendMail(SendMailRequest.builder()
                .to(message.getRecipients())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }
}

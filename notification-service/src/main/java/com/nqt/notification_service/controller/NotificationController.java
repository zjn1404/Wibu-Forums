package com.nqt.notification_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.notification_service.dto.request.MarkNotificationAsReadRequest;
import com.nqt.notification_service.dto.request.SendMailRequest;
import com.nqt.notification_service.dto.response.ApiResponse;
import com.nqt.notification_service.dto.response.NotificationResponse;
import com.nqt.notification_service.dto.response.PageResponse;
import com.nqt.notification_service.service.mail.MailService;
import com.nqt.notification_service.service.notification.NotificationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    MailService mailService;

    NotificationService notificationService;

    @NonFinal
    @Value("${message.controller.notification.mark-as-read-success}")
    String markAsReadSuccessMessage;

    @KafkaListener(topics = {"${app.kafka.topics.verification-mail}", "${app.kafka.topics.send-verification-code}"})
    public void listenVerificationMail(NotificationEvent message) {

        mailService.sendMail(SendMailRequest.builder()
                .to(message.getRecipients())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }

    @KafkaListener(topics = "${app.kafka.topics.notification}", groupId = "notification-group")
    public void listenNotification(NotificationEvent message) {
        notificationService.processNotification(message);
    }

    @GetMapping("/unread-notifications")
    public ApiResponse<PageResponse<NotificationResponse>> getUnreadNotifications(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return ApiResponse.<PageResponse<NotificationResponse>>builder()
                .result(notificationService.findAllUnreadNotifications(page, size))
                .build();
    }

    @PutMapping("/mark-as-read")
    public ApiResponse<Void> markAsRead(@RequestBody MarkNotificationAsReadRequest request) {
        notificationService.markNotificationAsRead(request);

        return ApiResponse.<Void>builder().message(markAsReadSuccessMessage).build();
    }
}

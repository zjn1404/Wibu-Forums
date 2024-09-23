package com.nqt.notification_service.service.notification;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.event.dto.Recipient;
import com.nqt.notification_service.dto.request.MarkNotificationAsReadRequest;
import com.nqt.notification_service.dto.response.NotificationResponse;
import com.nqt.notification_service.dto.response.PageResponse;
import com.nqt.notification_service.entity.Notification;
import com.nqt.notification_service.mapper.NotificationMapper;
import com.nqt.notification_service.repository.NotificationRepository;
import com.nqt.notification_service.utils.formatter.DateFormatter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImp implements NotificationService {

    @NonFinal
    String defaultSortProperty = "created_date";

    @NonFinal
    @Value("${app.socket.destination.notification}")
    String socketNotificationDestination;

    NotificationRepository notificationRepository;

    NotificationMapper notificationMapper;

    SimpMessagingTemplate messagingTemplate;

    DateFormatter dateFormatter;

    @Override
    public void processNotification(NotificationEvent notificationEvent) {
        Notification notification = notificationMapper.toNotification(notificationEvent);
        notification.setRecipientsReadStatus(buildRecipientsReadStatus(notificationEvent.getRecipients()));
        notification.setCreatedDate(new Date());
        notificationRepository.save(notification);

        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
        notificationResponse.setFormatedCreatedDate(dateFormatter.format(notification.getCreatedDate()));

        for (Recipient recipient : notificationEvent.getRecipients()) {
            messagingTemplate.convertAndSendToUser(
                    recipient.getUserId(), socketNotificationDestination, notificationResponse);
        }
    }

    @Override
    public void markNotificationAsRead(MarkNotificationAsReadRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Notification> notifications = notificationRepository.findAllById(request.getNotificationIds());
        for (Notification notification : notifications) {
            notification.getRecipientsReadStatus().put(authentication.getName(), true);
        }

        notificationRepository.saveAll(notifications);
    }

    @Override
    public PageResponse<NotificationResponse> findAllUnreadNotifications(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort = Sort.by(Sort.Direction.DESC, defaultSortProperty);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Notification> notifications =
                notificationRepository.findAllUnreadNotifications(authentication.getName(), pageable);

        List<NotificationResponse> notificationResponses = notifications.getContent().stream()
                .map(notification -> {
                    NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
                    notificationResponse.setFormatedCreatedDate(dateFormatter.format(notification.getCreatedDate()));
                    return notificationResponse;
                })
                .toList();

        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .totalPages(notifications.getTotalPages())
                .pageSize(size)
                .totalElements(notifications.getTotalElements())
                .data(notificationResponses)
                .build();
    }

    private Map<String, Boolean> buildRecipientsReadStatus(List<Recipient> recipients) {
        return recipients.stream().collect(Collectors.toMap(Recipient::getUserId, recipient -> false));
    }
}

package com.nqt.notification_service.dto.request;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarkNotificationAsReadRequest {
    List<String> notificationIds;
}

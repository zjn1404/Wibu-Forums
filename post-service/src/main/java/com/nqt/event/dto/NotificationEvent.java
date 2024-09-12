package com.nqt.event.dto;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
    String channel;
    List<Recipient> recipients;
    String subject;
    String componentId;
    String body;
}

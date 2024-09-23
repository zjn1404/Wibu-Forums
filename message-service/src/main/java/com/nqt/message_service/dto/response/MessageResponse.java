package com.nqt.message_service.dto.response;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    String id;

    String chatId;

    String senderId;

    String recipientId;

    Boolean readStatus;

    List<String> images;

    List<String> attachments;

    String content;

    String formatedSentDate;
}

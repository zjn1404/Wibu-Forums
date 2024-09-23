package com.nqt.message_service.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("chat-room")
public class ChatRoom {
    @MongoId
    String id;

    @Field("chat_id")
    String chatId;

    @Field("sender_id")
    String senderId;

    @Field("recipient_id")
    String recipientId;
}

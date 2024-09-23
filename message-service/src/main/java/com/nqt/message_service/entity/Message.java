package com.nqt.message_service.entity;

import java.util.Date;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Document("message")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {

    @MongoId
    String id;

    @Field("chat_id")
    String chatId;

    @Field("sender_id")
    String senderId;

    @Field("recipient_id")
    String recipientId;

    @Field("read_status")
    Boolean readStatus;

    @Field("images")
    List<Binary> images;

    @Field("attachments")
    List<Binary> attachments;

    @Field("content")
    String content;

    @Field("sent_date")
    Date sentDate;
}

package com.nqt.notification_service.entity;

import java.util.Date;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Document("notification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @MongoId
    String id;

    @Field("channel")
    String channel;

    @Field("subject")
    String subject;

    @Field("recipients_read_status")
    Map<String, Boolean> recipientsReadStatus;

    @Field("component_id")
    String componentId;

    @Field("body")
    String body;

    @Field("created_date")
    Date createdDate;
}

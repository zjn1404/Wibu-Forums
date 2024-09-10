package com.nqt.notification_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.Map;

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

    @Field("recipients")
    Map<String, Boolean> recipientsReadStatus;

    @Field("body")
    String body;

    @Field("created_date")
    Date createdDate;
}

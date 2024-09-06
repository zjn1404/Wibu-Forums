package com.nqt.post_service.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Document("comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @MongoId
    @Field(name = "id")
    String id;

    @Field(name = "user-id")
    String userId;

    @Field(name = "content")
    String content;

    @Field(name = "posted-date")
    Date postedDate;

    @Field(name = "modified-date")
    Date modifiedDate;

    @Field(name = "post-id")
    String postId;
}

package com.nqt.post_service.entity;

import java.util.Date;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Document("post")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @MongoId
    String id;

    @Field(name = "user-id")
    String userId;

    @Field(name = "content")
    String content;

    @Field(name = "images")
    List<Binary> images;

    @Field(name = "posted-date")
    Date postedDate;

    @Field(name = "modified-date")
    Date modifiedDate;
}

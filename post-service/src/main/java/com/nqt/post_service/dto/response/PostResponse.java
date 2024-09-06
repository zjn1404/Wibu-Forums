package com.nqt.post_service.dto.response;

import java.util.Date;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String content;
    List<String> images;
    String userId;
    String formattedPostedDate;
    Date postedDate;
    Date modifiedDate;
}

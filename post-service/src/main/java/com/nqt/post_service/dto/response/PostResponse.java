package com.nqt.post_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
    String userId;
    String formattedPostedDate;
    Date postedDate;
    Date modifiedDate;
}

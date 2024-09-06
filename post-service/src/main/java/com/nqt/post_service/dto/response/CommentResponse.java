package com.nqt.post_service.dto.response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String content;
    String userId;
    String formattedPostedDate;
    Date postedDate;
    Date modifiedDate;
}

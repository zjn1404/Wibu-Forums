package com.nqt.post_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostUpdateRequest {
    String content;
}

package com.nqt.notification_service.dto.request;

import java.util.List;

import com.nqt.event.dto.Recipient;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMailRequest {
    List<Recipient> to;
    String subject;
    String htmlContent;
}

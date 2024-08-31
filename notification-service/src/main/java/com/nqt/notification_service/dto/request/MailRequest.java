package com.nqt.notification_service.dto.request;

import java.util.List;

import com.nqt.event.dto.Recipient;
import com.nqt.notification_service.dto.request.sendmailrequestparam.Sender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailRequest {
    Sender sender;
    List<Recipient> to;
    String subject;
    String htmlContent;
}

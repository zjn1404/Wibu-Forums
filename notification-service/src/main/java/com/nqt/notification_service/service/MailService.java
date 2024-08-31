package com.nqt.notification_service.service;

import com.nqt.notification_service.dto.request.SendMailRequest;
import com.nqt.notification_service.dto.response.MailResponse;

public interface MailService {
    MailResponse sendMail(SendMailRequest request);
}

package com.nqt.notification_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nqt.notification_service.dto.request.MailRequest;
import com.nqt.notification_service.dto.request.SendMailRequest;
import com.nqt.notification_service.dto.request.sendmailrequestparam.Sender;
import com.nqt.notification_service.dto.response.MailResponse;
import com.nqt.notification_service.exception.AppException;
import com.nqt.notification_service.exception.ErrorCode;
import com.nqt.notification_service.repository.httpClient.MailClient;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImp implements MailService {

    @NonFinal
    @Value("${app.email.api-key}")
    String apiKey;

    @NonFinal
    @Value("${app.email.sender.name}")
    String senderName;

    @NonFinal
    @Value("${app.email.sender.email}")
    String senderEmail;

    MailClient mailClient;

    @Override
    public MailResponse sendMail(SendMailRequest request) {
        MailRequest mailRequest = MailRequest.builder()
                .sender(Sender.builder().name(senderName).email(senderEmail).build())
                .to(request.getTo())
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return mailClient.sendEmail(apiKey, mailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_MAIL);
        }
    }
}

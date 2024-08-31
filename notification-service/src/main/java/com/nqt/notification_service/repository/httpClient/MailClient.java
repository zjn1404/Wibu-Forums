package com.nqt.notification_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.nqt.notification_service.dto.request.MailRequest;
import com.nqt.notification_service.dto.response.MailResponse;

@FeignClient(name = "mail-client", url = "https://api.brevo.com")
public interface MailClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    MailResponse sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody MailRequest mailRequest);
}

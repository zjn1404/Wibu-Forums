package com.nqt.notification_service.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nqt.notification_service.dto.request.SendMailRequest;
import com.nqt.notification_service.dto.response.ApiResponse;
import com.nqt.notification_service.dto.response.MailResponse;
import com.nqt.notification_service.service.mail.MailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailController {
    MailService mailService;

    @PostMapping("/send")
    public ApiResponse<MailResponse> sendMail(@RequestBody SendMailRequest request) {
        return ApiResponse.<MailResponse>builder()
                .result(mailService.sendMail(request))
                .build();
    }

    @KafkaListener(topics = "onboard-successful")
    public void listen(String message) {
        log.info("Message received: {}", message);
    }
}

package com.nqt.identity_service.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.nqt.identity_service.entity.User;
import com.nqt.identity_service.entity.VerifyCode;
import com.nqt.identity_service.repository.VerifyCodeRepository;

import lombok.experimental.NonFinal;

@Component
public class Utils {

    private final VerifyCodeRepository verifyCodeRepository;

    @NonFinal
    @Value("${app.verify-code.valid-duration}")
    long verifyCodeValidDuration;

    @NonFinal
    @Value("${api-gateway.port}")
    int apiGatewayPort;

    @NonFinal
    @Value("${server.servlet.context-path}")
    String contextPath;

    @NonFinal
    @Value("${api-gateway.prefix}")
    String apiGatewayPrefix;

    public Utils(VerifyCodeRepository verifyCodeRepository) {
        this.verifyCodeRepository = verifyCodeRepository;
    }

    public String buildUserId(User user) {
        return String.format("%s%s", user.getUsername(), user.getPhoneNumber());
    }

    public String buildVerificationMailBody(User user) {

        UriComponents uriComponents =
                ServletUriComponentsBuilder.fromCurrentContextPath().build();

        final String baseUrl = String.format(
                "%s://%s:%s%s%s",
                uriComponents.getScheme(), uriComponents.getHost(), apiGatewayPort, apiGatewayPrefix, contextPath);

        VerifyCode verifyCode = generateVerifyCode(user);

        String mailContent = "<p>Dear " + user.getUsername() + ",</p>";
        mailContent += "<p> Please click the link below to verify your registration.</p>";

        String verifyUrl = baseUrl + "/verify?code=" + verifyCode.getVerifyCode() + "&user=" + user.getId();
        mailContent += "<h3><a href=\"" + verifyUrl + "\">VERIFY</a></h3>";
        mailContent += "<p> Thank you! <br> The Wibu Forums Team <p>";

        return mailContent;
    }

    public String buildVerifyCodeMail(User user) {

        VerifyCode verifyCode = generateVerifyCode(user);

        String mailContent = "<p>Dear " + user.getUsername() + ",</p>";
        mailContent += "<p> You Verification Code: " + verifyCode.getVerifyCode() + "</p>";
        mailContent += "<p> Thank you! <br> The Wibu Forums Team <p>";

        return mailContent;
    }

    public VerifyCode generateVerifyCode(User user) {
        Date expiryTime = new Date(
                Instant.now().plus(verifyCodeValidDuration, ChronoUnit.SECONDS).toEpochMilli());

        return verifyCodeRepository.save(VerifyCode.builder()
                .verifyCode(UUID.randomUUID().toString())
                .user(user)
                .expiryTime(expiryTime)
                .build());
    }
}

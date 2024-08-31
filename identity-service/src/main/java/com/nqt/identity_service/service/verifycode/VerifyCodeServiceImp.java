package com.nqt.identity_service.service.verifycode;

import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.nqt.event.dto.NotificationEvent;
import com.nqt.event.dto.Recipient;
import com.nqt.identity_service.entity.User;
import com.nqt.identity_service.entity.VerifyCode;
import com.nqt.identity_service.exception.AppException;
import com.nqt.identity_service.exception.ErrorCode;
import com.nqt.identity_service.repository.UserRepository;
import com.nqt.identity_service.repository.VerifyCodeRepository;
import com.nqt.identity_service.utils.Utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerifyCodeServiceImp implements VerifyCodeService {

    VerifyCodeRepository verifyCodeRepository;
    UserRepository userRepository;

    Utils utils;

    KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public boolean verify(String code, String userId) {
        VerifyCode verifyCode = verifyCodeRepository
                .findByVerifyCodeAndUserId(code, userId)
                .orElseThrow(() -> new AppException(ErrorCode.VERIFY_CODE_INCORRECT));

        if (verifyCode.getExpiryTime().before(new Date())) {
            User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            verifyCodeRepository.deleteById(verifyCode.getVerifyCode());
            sendVerifyMail(user);

            return false;
        }

        verifyCodeRepository.delete(verifyCode);

        return true;
    }

    @Override
    public void sendVerifyMail(User user) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recipients(List.of(Recipient.builder()
                        .name(user.getUsername())
                        .email(user.getEmail())
                        .build()))
                .subject("VERIFICATION MAIL")
                .body(utils.buildVerificationMailBody(user))
                .build();

        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

    @Override
    @Transactional
    public void deleteVerifyCode(String userId) {
        verifyCodeRepository.deleteByUserId(userId);
    }
}

package com.nqt.identity_service.service.cleanup;

import java.util.Date;

import jakarta.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nqt.identity_service.repository.InvalidatedTokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpiredTokenCleanupService {

    InvalidatedTokenRepository tokenRepository;

    @Scheduled(fixedDelay = 86400000) // ms
    @Transactional
    public void cleanExpiredTokens() {
        tokenRepository.deleteAllByExpiryTimeBefore(new Date());
    }
}

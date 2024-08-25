package com.nqt.identity_service.service.cleanup;

import com.nqt.identity_service.repository.InvalidatedTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

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

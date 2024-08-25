package com.nqt.identity_service.repository;

import com.nqt.identity_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    boolean existsByRfId(String rfId);

    void deleteAllByExpiryTimeBefore(Date date);
}

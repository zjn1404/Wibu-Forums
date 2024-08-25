package com.nqt.identity_service.repository;

import com.nqt.identity_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    boolean existsByRfId(String rfId);

    void deleteAllByExpiryTimeBefore(Date date);
}

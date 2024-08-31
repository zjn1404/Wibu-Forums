package com.nqt.identity_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqt.identity_service.entity.VerifyCode;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, String> {
    Optional<VerifyCode> findByVerifyCodeAndUserId(String verifyCode, String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}

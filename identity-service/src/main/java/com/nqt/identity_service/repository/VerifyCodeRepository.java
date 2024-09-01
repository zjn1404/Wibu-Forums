package com.nqt.identity_service.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nqt.identity_service.entity.VerifyCode;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, String> {
    Optional<VerifyCode> findByVerifyCodeAndUserId(String verifyCode, String userId);

    boolean existsByUserId(String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM verify_code vc WHERE vc.user.id = :userId")
    void deleteByUserId(@Param("userId") String userId);
}

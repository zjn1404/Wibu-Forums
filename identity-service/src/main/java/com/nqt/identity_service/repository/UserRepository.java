package com.nqt.identity_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqt.identity_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);

    Optional<User> findByUsername(String username);
}

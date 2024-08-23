package com.nqt.identity_service.repository;

import com.nqt.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);

    Optional<User> findByUsername(String username);
}

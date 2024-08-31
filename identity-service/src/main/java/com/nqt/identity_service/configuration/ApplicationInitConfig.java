package com.nqt.identity_service.configuration;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nqt.identity_service.entity.Role;
import com.nqt.identity_service.entity.User;
import com.nqt.identity_service.repository.RoleRepository;
import com.nqt.identity_service.repository.UserRepository;
import com.nqt.identity_service.utils.Utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    UserRepository userRepository;
    RoleRepository roleRepository;

    Utils utils;

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            @Value("${init-info.role}") String adminRole,
            @Value("${init-info.username}") String username,
            @Value("${init-info.password}") String password,
            @Value("${init-info.email}") String email,
            @Value("${init-info.phone-number}") String phoneNumber) {
        return args -> {
            if (!roleRepository.existsByName(adminRole)) {
                Role role = Role.builder().name(adminRole).build();
                roleRepository.save(role);

                User user = User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .email(email)
                        .phoneNumber(phoneNumber)
                        .roles(new HashSet<>(List.of(role)))
                        .build();

                user.setId(utils.buildUserId(user));

                userRepository.save(user);
            }
        };
    }
}

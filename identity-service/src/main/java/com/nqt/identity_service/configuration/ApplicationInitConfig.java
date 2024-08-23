package com.nqt.identity_service.configuration;

import com.nqt.identity_service.entity.Role;
import com.nqt.identity_service.entity.User;
import com.nqt.identity_service.repository.RoleRepository;
import com.nqt.identity_service.repository.UserRepository;
import com.nqt.identity_service.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    UserRepository userRepository;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(
            @Value("${init-info.role}") String adminRole,
            @Value("${init-info.username}") String username,
            @Value("${init-info.password}") String password,
            @Value("${init-info.email}") String email,
            @Value("${init-info.phone-number}") String phoneNumber,
            @Value("${init-info.first-name}") String firstName,
            @Value("${init-info.last-name}") String lastName
    ) {
        return args -> {
            if (!roleRepository.existsByName(adminRole)) {
                Role role = Role.builder().name(adminRole).build();
                roleRepository.save(role);

                User user = User.builder()
                        .username(username)
                        .password(password)
                        .email(email)
                        .phoneNumber(phoneNumber)
                        .firstName(firstName)
                        .lastName(lastName)
                        .roles(new HashSet<>(List.of(role)))
                        .build();

                user.setId(Utils.buildUserId(user));

                userRepository.save(user);
            }
        };
    }
}

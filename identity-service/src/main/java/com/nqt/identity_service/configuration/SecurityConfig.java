package com.nqt.identity_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_POST_ENDPOINTS = {
        "/users/registration",
        "/auth/token",
        "/auth/refresh",
        "/auth/change-password",
        "/auth/logout",
        "/auth/introspect",
        "/auth/outbound/authentication"
    };

    private static final String[] PUBLIC_GET_ENDPOINTS = {"/users/{id}"};

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomJwtDecoder jwtDecoder,
            AfterBearerTokenAuthenticationFilterExceptionHandler exceptionHandler)
            throws Exception {

        http.addFilterBefore(exceptionHandler, LogoutFilter.class);

        http.authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS)
                .permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());

        http.oauth2ResourceServer(configurer -> configurer
                .jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return converter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

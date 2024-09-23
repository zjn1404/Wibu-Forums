package com.nqt.message_service.configuration;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nqt.message_service.dto.response.ApiResponse;
import com.nqt.message_service.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AfterBearerTokenAuthenticationFilterExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {

            ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

            response.setStatus(errorCode.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

            response.getWriter().flush();
        }
    }
}

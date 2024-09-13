package com.nqt.api_gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nqt.api_gateway.dto.response.ApiResponse;
import com.nqt.api_gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @NonFinal
    @Value("${code.unauthenticated}")
    int unauthenticatedCode;

    @NonFinal
    @Value("${message.unauthenticated}")
    String unauthenticatedMessage;

    @NonFinal
    String[] publicEndpoints = {
            "/identity/users/registration", "/identity/auth/.*", "/notification/email/send", "/identity/verify.*", "/notification/ws/.*"
    };

    @NonFinal
    @Value("${app.api-prefix}")
    String prefixApi;

    IdentityService identityService;
    ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeaders)) {
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeaders.get(0).replace("Bearer", "");

        return identityService.introspect(token).flatMap(introspectResponse -> {
            if (introspectResponse.getResult().isValid()) {
                return chain.filter(exchange);
            }

            return unauthenticated(exchange.getResponse());
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String requestPath = request.getURI().getPath();

        return Arrays.stream(publicEndpoints)
                .anyMatch(pattern -> {
                    String fullPattern = prefixApi + pattern;
                    return requestPath.matches(fullPattern);
                });
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(unauthenticatedCode)
                .message(unauthenticatedMessage)
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }
}

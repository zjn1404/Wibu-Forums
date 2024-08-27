package com.nqt.api_gateway.service;

import com.nqt.api_gateway.dto.response.ApiResponse;
import com.nqt.api_gateway.dto.response.IntrospectResponse;
import reactor.core.publisher.Mono;

public interface IdentityService {

    Mono<ApiResponse<IntrospectResponse>> introspect(String token);

}

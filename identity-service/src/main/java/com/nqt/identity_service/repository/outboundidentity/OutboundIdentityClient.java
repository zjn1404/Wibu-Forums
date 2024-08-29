package com.nqt.identity_service.repository.outboundidentity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.nqt.identity_service.dto.request.ExchangeTokenRequest;
import com.nqt.identity_service.dto.response.ExchangeTokenResponse;

import feign.QueryMap;

@FeignClient(name = "outbound-client", url = "${outbound.identity.base_uri}")
public interface OutboundIdentityClient {
    @PostMapping(
            value = "/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest exchangeTokenRequest);
}

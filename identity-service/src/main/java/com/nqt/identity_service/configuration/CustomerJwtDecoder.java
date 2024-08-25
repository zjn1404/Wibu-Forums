package com.nqt.identity_service.configuration;

import com.nqt.identity_service.dto.request.security.IntrospectRequest;
import com.nqt.identity_service.exception.ErrorCode;
import com.nqt.identity_service.service.authentication.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerJwtDecoder implements JwtDecoder {

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder = null;

    AuthenticationService authenticationService;

    @Override
    public Jwt decode(String token) throws JwtException {
        boolean isValid = authenticationService.introspect(IntrospectRequest.builder()
                .token(token).build()).isValid();

        if (!isValid) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "SH512");

            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}

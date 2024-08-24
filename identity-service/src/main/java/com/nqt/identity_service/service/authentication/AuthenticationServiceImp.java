package com.nqt.identity_service.service.authentication;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nqt.identity_service.dto.request.security.*;
import com.nqt.identity_service.dto.response.AuthenticationResponse;
import com.nqt.identity_service.dto.response.IntrospectResponse;
import com.nqt.identity_service.entity.InvalidatedToken;
import com.nqt.identity_service.entity.User;
import com.nqt.identity_service.exception.AppException;
import com.nqt.identity_service.exception.ErrorCode;
import com.nqt.identity_service.repository.InvalidatedTokenRepository;
import com.nqt.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImp implements AuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    String accessSignerKey;

    @NonFinal
    @Value("${jwt.refreshSignerKey}")
    String refreshSignerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    long validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    long refreshableDuration;

    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        return buildAuthenticationResponse(user);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        try {
            SignedJWT token = verifyToken(request.getToken(), true);

            String id = token.getJWTClaimsSet().getJWTID();
            String acId = token.getJWTClaimsSet().getClaim("acId").toString();
            Date exp = token.getJWTClaimsSet().getExpirationTime();

            invalidatedTokenRepository.save(InvalidatedToken.builder()
                            .acId(acId)
                            .rfId(id)
                            .expiryTime(exp)
                            .build());

            return buildAuthenticationResponse(userRepository.findByUsername(token.getJWTClaimsSet().getSubject())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (Exception e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.THE_SAME_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.getToken());

            String acId = signedJWT.getJWTClaimsSet().getJWTID();
            String rfId = signedJWT.getJWTClaimsSet().getClaim("rfId").toString();
            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            exp = new Date(exp.getTime() + (refreshableDuration - validDuration) * 1000);

            invalidatedTokenRepository.save(InvalidatedToken.builder()
                            .acId(acId)
                            .rfId(rfId)
                            .expiryTime(exp)
                            .build());

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private AuthenticationResponse buildAuthenticationResponse(User user) {
        String acId = UUID.randomUUID().toString();
        String rfId = UUID.randomUUID().toString();

        return AuthenticationResponse.builder()
                .accessToken(generateToken(user, acId, rfId, accessSignerKey, validDuration))
                .refreshToken(generateToken(user, rfId, acId, refreshSignerKey, refreshableDuration))
                .isSuccess(true)
                .build();
    }

    private String generateToken(User user, String id, String otherId, String signerKey, long duration){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet;

        if (signerKey.equals(accessSignerKey)) {
            claimsSet = buildAccessTokenClaims(user, id, otherId, duration);
        } else {
            claimsSet = buildRefreshTokenClaims(user, id, otherId, duration);
        }

        Payload payload = claimsSet.toPayload();

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private JWTClaimsSet buildAccessTokenClaims(User user, String id, String otherId, long duration) {
        return new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .jwtID(id)
                .issuer("nqt.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("rfId", otherId)
                .claim("scope", buildScope(user))
                .build();
    }

    private JWTClaimsSet  buildRefreshTokenClaims(User user, String id, String otherId, long duration) {
        return new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .jwtID(id)
                .issuer("nqt.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("acId", otherId)
                .claim("scope", buildScope(user))
                .build();
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        user.getRoles().forEach(
                role -> {
                    joiner.add("ROLE_" + role.getName());
                    if (!CollectionUtils.isEmpty(role.getPermissions())) {
                        role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                    }
                }
        );
        return joiner.toString();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(accessSignerKey);

        if (isRefresh) {
            verifier = new MACVerifier(refreshSignerKey);
        }

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date duration = signedJWT.getJWTClaimsSet().getExpirationTime();

        String id = signedJWT.getJWTClaimsSet().getJWTID();
        boolean verified = signedJWT.verify(verifier);

        if (!verified || duration.before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (invalidatedTokenRepository.existsById(id) || invalidatedTokenRepository.existsByRfId(id)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return signedJWT;
    }
}

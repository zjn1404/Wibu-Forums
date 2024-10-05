package com.nqt.identity_service.service.authentication;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nqt.identity_service.dto.request.ExchangeTokenRequest;
import com.nqt.identity_service.dto.request.security.*;
import com.nqt.identity_service.dto.request.user.UserCreationRequest;
import com.nqt.identity_service.dto.response.AuthenticationResponse;
import com.nqt.identity_service.dto.response.ExchangeTokenResponse;
import com.nqt.identity_service.dto.response.IntrospectResponse;
import com.nqt.identity_service.dto.response.OutboundUserResponse;
import com.nqt.identity_service.entity.InvalidatedToken;
import com.nqt.identity_service.entity.User;
import com.nqt.identity_service.entity.VerifyCode;
import com.nqt.identity_service.exception.AppException;
import com.nqt.identity_service.exception.ErrorCode;
import com.nqt.identity_service.repository.InvalidatedTokenRepository;
import com.nqt.identity_service.repository.UserRepository;
import com.nqt.identity_service.repository.VerifyCodeRepository;
import com.nqt.identity_service.repository.outboundidentity.OutboundIdentityClient;
import com.nqt.identity_service.repository.outboundidentity.OutboundUserClient;
import com.nqt.identity_service.service.user.UserService;
import com.nqt.identity_service.service.verifycode.VerifyCodeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImp implements AuthenticationService {

    @NonFinal
    @Value("${app.roles.user}")
    String userRole;

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

    @NonFinal
    @Value("${outbound.identity.client_id}")
    String clientId;

    @NonFinal
    @Value("${outbound.identity.client_secret}")
    String clientSecret;

    @NonFinal
    @Value("${outbound.identity.redirect_uri}")
    String redirectUri;

    @NonFinal
    @Value("${outbound.identity.grant_type}")
    String grantType;

    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRepository userRepository;
    VerifyCodeRepository verifyCodeRepository;

    OutboundIdentityClient outBoundIdentityClient;
    OutboundUserClient outboundUserClient;

    UserService userService;

    VerifyCodeService verifyCodeService;

    PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        VerifyCode verifyCode =
                verifyCodeRepository.findVerifyCodeByUserId(user.getId()).orElse(null);
        if (verifyCode != null) {
            if (verifyCode.getExpiryTime().before(new Date())) {
                verifyCodeRepository.delete(verifyCode);
                verifyCodeService.sendVerifyMail(user);
            }
            throw new AppException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        return buildAuthenticationResponse(user);
    }

    @Override
    public AuthenticationResponse outboundAuthenticate(String code) {
        ExchangeTokenResponse exchangeTokenResponse =
                outBoundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .redirectUri(redirectUri)
                        .grantType(grantType)
                        .code(code)
                        .build());

        OutboundUserResponse userInfo = outboundUserClient.getUserInfo("json", exchangeTokenResponse.getAccessToken());

        User user = userRepository
                .findByEmail(userInfo.getEmail())
                .orElseGet(() -> userService.createInternalUser(UserCreationRequest.builder()
                        .email(userInfo.getEmail())
                        .username(userInfo.getEmail())
                        .firstName(userInfo.getFamilyName())
                        .lastName(userInfo.getGivenName())
                        .roles(new HashSet<>(List.of(userRole)))
                        .build()));

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

            return buildAuthenticationResponse(userRepository
                    .findById(token.getJWTClaimsSet().getSubject())
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

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public void createPassword(PasswordCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (StringUtils.hasText(user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findById(authentication.getName())
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

    private String generateToken(User user, String id, String otherId, String signerKey, long duration) {

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
                .subject(user.getId())
                .jwtID(id)
                .issuer("nqt.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("rfId", otherId)
                .claim("scope", buildScope(user))
                .build();
    }

    private JWTClaimsSet buildRefreshTokenClaims(User user, String id, String otherId, long duration) {
        return new JWTClaimsSet.Builder()
                .subject(user.getId())
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
        user.getRoles().forEach(role -> {
            joiner.add("ROLE_" + role.getName());
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
            }
        });
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

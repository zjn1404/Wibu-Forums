package com.nqt.identity_service.service.authentication;

import com.nqt.identity_service.dto.request.security.*;
import com.nqt.identity_service.dto.response.AuthenticationResponse;
import com.nqt.identity_service.dto.response.IntrospectResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse outboundAuthenticate(String code);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    void changePassword(ChangePasswordRequest request);

    void logout(LogoutRequest request);
}

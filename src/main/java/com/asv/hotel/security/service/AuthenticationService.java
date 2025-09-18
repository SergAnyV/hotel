package com.asv.hotel.security.service;

import com.asv.hotel.security.domain.JWTAuthenticationResponse;

public interface AuthenticationService {
    JWTAuthenticationResponse signIn(String nickName, String password);
    JWTAuthenticationResponse refreshAccessToken(String refreshToken);
}

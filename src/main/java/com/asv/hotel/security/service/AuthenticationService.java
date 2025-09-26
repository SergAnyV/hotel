package com.asv.hotel.security.service;

import com.asv.hotel.security.domain.JWTAuthentication;
import com.asv.hotel.security.domain.RefreshRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    JWTAuthentication signIn(String nickName, String password);
    JWTAuthentication refreshAccessToken(String refreshToken);
    void removeTokensFromStorage(HttpServletRequest request);
}

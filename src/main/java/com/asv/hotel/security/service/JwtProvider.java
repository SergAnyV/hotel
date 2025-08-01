package com.asv.hotel.security.service;

import com.asv.hotel.entities.User;
import io.jsonwebtoken.Claims;
import lombok.NonNull;


public interface JwtProvider {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateAccessToken(String accessToken);

    boolean validateRefreshToken(String refreshToken);

    Claims getAccessClaims(String token);

    Claims getRefreshClaims(String token);


}

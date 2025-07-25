package com.asv.hotel.security.service;

import com.asv.hotel.entities.User;
import io.jsonwebtoken.Claims;
import lombok.NonNull;

public interface JwtProovider {

    //    the method generates token for access
    String generateAccessToken(User user);

    //    the method generates refresh token after expired accessToken
    String generateRefreshToken(User user);

    //    the method check sthe token for validating of access Token and return boolean value
    boolean validateAccessToken(String accessToken);

    //    the method checks the token for validating of refresh Token and return boolean value
    boolean validateRefreshToken(String refreshToken);

    //    return claims from token
    Claims getAccessClaims(String token);

    //    return claims from token
    Claims getRefreshClaims( String token);


}

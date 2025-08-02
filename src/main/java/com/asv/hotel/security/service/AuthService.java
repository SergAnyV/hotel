package com.asv.hotel.security.service;

import com.asv.hotel.entities.UserType;
import com.asv.hotel.security.domain.JwtAuthentication;
import com.asv.hotel.security.domain.JwtRequest;
import com.asv.hotel.security.domain.JwtResponse;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface AuthService {

    JwtResponse login( JwtRequest authRequest);
    JwtResponse getAccessToken(String refreshToken);
    JwtResponse refresh(String refreshToken);
    JwtAuthentication getAuthInfo();
    JwtAuthentication generate(Claims claims);
}

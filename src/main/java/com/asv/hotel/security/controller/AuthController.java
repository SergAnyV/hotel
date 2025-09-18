package com.asv.hotel.security.controller;

import com.asv.hotel.security.domain.JWTAuthenticationResponse;
import com.asv.hotel.security.domain.LogoutRequest;
import com.asv.hotel.security.domain.RefreshTokenRequest;
import com.asv.hotel.security.domain.SignInRequest;
import com.asv.hotel.security.service.TokenStorageService;
import com.asv.hotel.security.service.impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;
    @Autowired
    private TokenStorageService tokenStorageService;

    // Эндпоинт для входа (выдает access + refresh токены)
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        JWTAuthenticationResponse response = authenticationService.signIn(request.getNickName(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    // НОВЫЙ Эндпоинт для обновления access токена
    @PostMapping("/refresh")
    public ResponseEntity<JWTAuthenticationResponse> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        JWTAuthenticationResponse response = authenticationService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        // Удаляем access токен из хранилища
        tokenStorageService.removeToken(request.getAccessToken());
        // Удаляем refresh токен из хранилища
        tokenStorageService.removeToken(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }

}

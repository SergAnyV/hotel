package com.asv.hotel.security.controller;

import com.asv.hotel.security.domain.JWTAuthentication;
import com.asv.hotel.security.domain.RefreshRequest;
import com.asv.hotel.security.domain.SignInRequest;
import com.asv.hotel.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

       // Эндпоинт для входа (выдает access + refresh токены)
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthentication> signIn(@RequestBody SignInRequest request) {
        JWTAuthentication response = authenticationService.signIn(request.getNickName(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    // НОВЫЙ Эндпоинт для обновления access токена
    @PostMapping("/refresh")
    public ResponseEntity<JWTAuthentication> refreshAccessToken(@RequestBody RefreshRequest refreshTokenRequest) {
        JWTAuthentication response = authenticationService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authenticationService.removeTokensFromStorage(request);
        return ResponseEntity.ok().build();
    }
}

package com.asv.hotel.security.controller;

import com.asv.hotel.security.domain.JWTAuthenticationResponse;
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

    // Класс для принятия данных при входе
    static class SignInRequest {
        private String nickName;
        private String password;

        // Геттеры и сеттеры
        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // НОВЫЙ класс для принятия refresh токена
    static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    // Класс для принятия токенов при выходе
    static class LogoutRequest {
        private String accessToken;
        private String refreshToken;

        // Геттеры и сеттеры
        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}

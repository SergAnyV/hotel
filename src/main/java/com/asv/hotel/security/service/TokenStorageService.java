package com.asv.hotel.security.service;

import java.util.Set;

//добавляем проверяем наличие удаляем и очищаем , дайствия для хранилища токенов
public interface TokenStorageService {
    void addTokens(Long userId, String token);
    boolean isTokenActive(String token);
    boolean isTokenActiveForUser(Long userId, String token);
    void removeToken(String token);
    void removeTokenForUser(Long userId, String token);
    void removeAllUserTokens(Long userId);
    void clearAllTokens();
    Set<String> getUserActiveTokens(Long userId);
}

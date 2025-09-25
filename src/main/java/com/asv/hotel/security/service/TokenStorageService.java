package com.asv.hotel.security.service;

//добавляем проверяем наличие удаляем и очищаем , дайствия для хранилища токенов
public interface TokenStorageService {
    void addToken(String token);

    boolean isTokenActive(String token);

    void removeToken(String token);

    void clearAllTokens();
}

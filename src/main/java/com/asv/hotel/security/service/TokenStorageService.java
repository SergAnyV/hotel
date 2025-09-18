package com.asv.hotel.security.service;

//добавляем проверяем наличие удаляем и очищаем , дайствия для хранилища токенов
public interface TokenStorageService<S> {
    void addToken(S token);

    boolean isTokenActive(S token);

    void removeToken(S token);

    void clearAllTokens();
}

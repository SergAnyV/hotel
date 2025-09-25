package com.asv.hotel.security.service.impl;

import com.asv.hotel.security.service.TokenStorageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class TokenStorageServiceImpl implements TokenStorageService{
    //для потокобезопасности потом можно канкарент хэшмап а лучше в редиску писать
    private final Map<String, Boolean> activeTokens = new HashMap<>();

    @Override
    public void addToken(String token) {
        activeTokens.put(token, true);
    }

    @Override
    public boolean isTokenActive(String token) {
        return activeTokens.containsKey(token);
    }
    @Override
    public void removeToken(String token) {
        activeTokens.remove(token);
    }

    @Override
    public void clearAllTokens() {
        activeTokens.clear();
    }

}

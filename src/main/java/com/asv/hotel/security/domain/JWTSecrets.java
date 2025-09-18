package com.asv.hotel.security.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JWTSecrets {
    // ключ для подписи
    @Value("${jwt.secret.access}")
    private String accessSecret;

    //  ключ для подписи после обновления основного ключа
    @Value("${jwt.secret.refresh}")
    private String refreshSecret;

}

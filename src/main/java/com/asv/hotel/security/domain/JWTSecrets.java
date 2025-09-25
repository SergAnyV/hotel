package com.asv.hotel.security.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "jwt.secret")
@Setter
public class JWTSecrets {
    // ключ для подписи
    private String access;

    //  ключ для подписи после обновления основного ключа
    private String refresh;

}

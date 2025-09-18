package com.asv.hotel.security.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class JWTAuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private static String TOKEN_TYPE = "Bearer";
}

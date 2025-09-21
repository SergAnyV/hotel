package com.asv.hotel.security.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class JWTAuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private static String TOKEN_TYPE = "Bearer ";
}

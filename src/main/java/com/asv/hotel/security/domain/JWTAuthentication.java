package com.asv.hotel.security.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class JWTAuthentication {
    private String accessToken;
    private String refreshToken;
    private static final String TOKEN_TYPE = "Bearer ";
}

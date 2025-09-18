package com.asv.hotel.security.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogoutRequest {
    private String accessToken;
    private String refreshToken;

}

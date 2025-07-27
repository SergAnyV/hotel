package com.asv.hotel.security.jwtdomain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {

    private String login;
    private String password;

}

package com.asv.hotel.security.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SignInRequest {
    private String nickName;
    private String password;
}

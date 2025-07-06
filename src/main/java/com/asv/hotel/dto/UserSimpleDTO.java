package com.asv.hotel.dto;

import com.asv.hotel.entities.UserType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
public class UserSimpleDTO {

    private String nickName;


    private String firstName;


    private String fathersName;


    private String lastName;


    private String email;


    private String phoneNumber;

    @EqualsAndHashCode.Exclude
    private UserType role;

}

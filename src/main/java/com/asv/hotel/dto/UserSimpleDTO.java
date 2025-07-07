package com.asv.hotel.dto;


import lombok.Builder;
import lombok.Data;



@Builder
@Data
public class UserSimpleDTO {

    private String nickName;


    private String firstName;


    private String fathersName;


    private String lastName;


    private String email;


    private String phoneNumber;


}

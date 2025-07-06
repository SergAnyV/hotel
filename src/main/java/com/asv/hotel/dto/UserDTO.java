package com.asv.hotel.dto;


import com.asv.hotel.entities.Report;
import com.asv.hotel.entities.UserType;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class UserDTO {


    private String nickName;


    private String firstName;


    private String fathersName;


    private String lastName;


    private String email;


    private String phoneNumber;


    private String password;


    private String role;

    @EqualsAndHashCode.Exclude
    private Set<BookingDTO> bookingDTOSet = new HashSet<>();


    @EqualsAndHashCode.Exclude
    private Set<Report> reports = new HashSet<>();
}

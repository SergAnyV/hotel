package com.asv.hotel.dto;


import com.asv.hotel.entities.Booking;
import com.asv.hotel.entities.Report;
import lombok.*;
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
// никаких сущностей
    @EqualsAndHashCode.Exclude
    private Set<Booking> bookingSet;

    // никаких сущностей
    @EqualsAndHashCode.Exclude
    private Set<Report> reports;
}

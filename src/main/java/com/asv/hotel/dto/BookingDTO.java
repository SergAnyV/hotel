package com.asv.hotel.dto;


import com.asv.hotel.entities.Service;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;

import java.util.Set;
@Data
@Builder
public class BookingDTO {

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer persons;

    private String roomNumber;

    private UserSimpleDTO userSimpleDTO;

    private PromoCodeDTO promoCodeDTO;

    private Set<Service> serviceSet ;

}

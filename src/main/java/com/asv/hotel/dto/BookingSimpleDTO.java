package com.asv.hotel.dto;

import com.asv.hotel.entities.Service;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class BookingSimpleDTO {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer persons;

    private BigDecimal totalPrice;

    private String statusOfBooking;

    private LocalDate createdAt;

    private RoomSimpleDTO roomSimpleDTO;

    private UserSimpleDTO userSimpleDTO;

    private PromoCodeDTO promoCodeDTO;

    private Set<Service> serviceSet = new HashSet<>();
}

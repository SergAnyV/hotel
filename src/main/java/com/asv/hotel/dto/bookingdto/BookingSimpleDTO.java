package com.asv.hotel.dto.bookingdto;

import com.asv.hotel.dto.promocodedto.PromoCodeDTO;
import com.asv.hotel.dto.roomdto.RoomSimpleDTO;
import com.asv.hotel.dto.userdto.UserSimpleDTO;
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

package com.asv.hotel.controllers;

import com.asv.hotel.dto.BookingDTO;
import com.asv.hotel.dto.BookingSimpleDTO;
import com.asv.hotel.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    ResponseEntity<BookingSimpleDTO> createBooking(@RequestBody BookingDTO bookingDTO){
        BookingSimpleDTO newBookingDTO=bookingService.save(bookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookingDTO);
    }
}

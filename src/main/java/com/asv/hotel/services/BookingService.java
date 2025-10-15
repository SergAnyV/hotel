package com.asv.hotel.services;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.bookingdto.ResponseBookingDTO;
import com.asv.hotel.dto.roomdto.RoomSimpleDataBaseDTO;
import jakarta.servlet.http.HttpServletRequest;


import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingSimplDTO bookingSimplDTO);

    void deleteBookingById(Long id);

    List<BookingSimplDTO> findAllBookingsSimplDTOByRoomNumber(String roomNumber);

    ResponseBookingDTO findBesponseBookingDTOByBookingId(Long id, HttpServletRequest request);

    List<RoomSimpleDataBaseDTO> findRoomSimpleDTODataBaseByBookingDate(LocalDate checkInDate, LocalDate checkOutDate);
}


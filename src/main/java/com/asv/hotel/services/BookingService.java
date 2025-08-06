package com.asv.hotel.services;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;

import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingSimplDTO bookingSimplDTO);

    void deleteBookingById(Long id);

    List<BookingSimplDTO> findAllBookingsSimplDTOByRoomNumber(String roomNumber);

}

package com.asv.hotel.services;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.bookingdto.ResponseBookingDTO;
import com.asv.hotel.dto.roomdto.RoomSimpleDataBaseDTO;
import com.asv.hotel.entities.Booking;
import com.asv.hotel.entities.User;


import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingSimplDTO bookingSimplDTO);

    void deleteBookingById(Long id);

    List<BookingSimplDTO> findAllBookingsSimpleDTOByRoomNumber(String roomNumber);

    ResponseBookingDTO findResponseBookingDTOByBookingId(Long id);

    List<RoomSimpleDataBaseDTO> findRoomSimpleDataBaseDTOByBookingDate(LocalDate checkInDate, LocalDate checkOutDate);

    Booking findBookingByIdOrNull(Long bookingId);

    User findUserOwnerOfBookingByIdOrNull(Long bookingId);
}


package com.asv.hotel.services;

import com.asv.hotel.dto.BookingDTO;
import com.asv.hotel.dto.BookingSimpleDTO;
import com.asv.hotel.dto.mapper.BookingMapper;
import com.asv.hotel.entities.Booking;
import com.asv.hotel.entities.Room;
import com.asv.hotel.entities.User;
import com.asv.hotel.repositories.BookingRepository;
import com.asv.hotel.util.BookingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Transactional
    public BookingSimpleDTO save(BookingDTO bookingDTO) {
        try {
            Booking booking = BookingMapper.INSTANCE.bookingDTOToBoking(bookingDTO);
            log.info("{}",bookingDTO);
            User user=userService.findUserByLastNameAndFirstNameReturnUser(bookingDTO.getUserSimpleDTO().getLastName()
                    ,bookingDTO.getUserSimpleDTO().getFirstName());

            Room room = roomService.findByNumberReturnRoom(bookingDTO.getRoomNumber());

            booking.setTotalPrice(room.getPricePerNight()
                    .multiply(BookingUtils.calculateLivingDays(bookingDTO.getCheckInDate(),bookingDTO.getCheckOutDate())));
            booking.setStatusOfBooking("Статус");
            booking.setUser(user);
            booking.setRoom(room);
            return BookingMapper.INSTANCE.bookingToBookingSimpleDTO(bookingRepository.save(booking));
        } catch (RuntimeException ex) {
            log.error("Error проблемы с сохранением бронирования, юзер {} {}"
                    ,bookingDTO.getUserSimpleDTO().getLastName()
            ,bookingDTO.getUserSimpleDTO().getFirstName());
            throw ex;
        }
    }


}

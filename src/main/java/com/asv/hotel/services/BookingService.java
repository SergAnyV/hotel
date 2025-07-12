package com.asv.hotel.services;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.mapper.BookingMapper;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.entities.Booking;
import com.asv.hotel.entities.Room;
import com.asv.hotel.entities.ServiceHotel;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.enums.BookingStatus;
import com.asv.hotel.exceptions.mistakes.DataNotFoundException;
import com.asv.hotel.repositories.BookingRepository;
import com.asv.hotel.util.BookingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final ServiceHotelService serviceHotelService;
    private final PromoCodeService promoCodeService;

    @Transactional
    public BookingDTO save(BookingSimplDTO bookingSimplDTO) {
        try {
            Room room = roomService.findByNumberReturnRoom(bookingSimplDTO.getRoomNumber());
            if (room.getIsAvailable() && !bookingRepository.isRoomAvailableForDates(room.getId()
                    , bookingSimplDTO.getCheckInDate()
                    , bookingSimplDTO.getCheckOutDate())) {
                throw new DataAccessException("комната не свободна на данные даты или ") {
                };
            }
            Booking booking = BookingMapper.INSTANCE.bookingSimpleDTOToBooking(bookingSimplDTO);
            User user = userService.findUserByLastNameAndFirstNameReturnUser(
                    bookingSimplDTO.getUserSimpleDTO().getLastName(), bookingSimplDTO.getUserSimpleDTO().getFirstName());
            booking.setUser(user);

            booking.setRoom(room);
            booking.setStatusOfBooking(BookingStatus.CONFIRMED);

            //расчет стоимости за номер
            Set<ServiceHotel> serviceHotel = findAllServices(bookingSimplDTO.getServiceSet());
            BigDecimal livingDays=BookingUtils.calculateLivingDays(bookingSimplDTO.getCheckInDate(), bookingSimplDTO.getCheckOutDate());
            BigDecimal totalPriceForServices=BigDecimal.ZERO;
            if (!serviceHotel.isEmpty()) {
                totalPriceForServices=serviceHotel.stream().map(serviceHotelentity -> {
                    return serviceHotelentity.getPrice().multiply(livingDays);
                }).reduce(BigDecimal.ZERO,(sum,price)->sum.add(price));
            }
            booking.setServiceSet(serviceHotel);
            booking.setTotalPrice(room.getPricePerNight()
                    .multiply(livingDays).add(totalPriceForServices));

            return BookingMapper.INSTANCE.bookingToBookingDTO(bookingRepository.save(booking));
        } catch (RuntimeException ex) {
            log.error("Error проблемы с сохранением бронирования");
            throw ex;
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            if (bookingRepository.deleteBookingById(id) == 0) {
                log.error("Error данной брони не существует для удаления {}", id);
                throw new DataNotFoundException("данной брони не существует для удаления");
            }
        } catch (DataAccessException ex) {
            log.error("Error проблема с удаление по Id бронирования {}", id, ex);
            throw new DataAccessException("проблема с удалением бронирования по id") {
            };
        }
    }

    @Transactional
    public List<BookingSimplDTO> findAllByRoomNumber(String roomNumber) {
        try {
            List<Booking> bookingsList = bookingRepository.findAllByRoomNumber(roomNumber);
            if (bookingsList.isEmpty()) {
                log.error("лист с бронированиями пуст для данной комнаты {}", roomNumber);
                throw new DataNotFoundException("лист с бронированиями пуст для данной комнаты");
            }
            return bookingsList.stream().map(b -> {
                        return BookingMapper.INSTANCE.bookingToBookingSimpleDTO(b);
                    })
                    .toList();
        } catch (DataAccessException ex) {
            log.error("Error проблема с поиском брони по номеру комнаты {}", roomNumber, ex);
            throw new DataAccessException("проблема с поиском брони по номеру комнаты") {
            };
        }
    }

    private Set<ServiceHotel> findAllServices(Set<ServiceHotelDTO> serviceHotelDTOS){
        if(serviceHotelDTOS.isEmpty()){
            return null;
        }
     return serviceHotelDTOS.stream().map(serviceHotelDTO -> {
            return serviceHotelService.findByTitleReturnEntity(serviceHotelDTO.getTitle());
        }).collect(Collectors.toSet());
    }

}

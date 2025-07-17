package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.mapper.BookingMapper;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelSimpleDTO;
import com.asv.hotel.entities.*;
import com.asv.hotel.entities.enums.BookingStatus;
import com.asv.hotel.exceptions.DataNotFoundException;
import com.asv.hotel.repositories.BookingRepository;
import com.asv.hotel.services.*;
import com.asv.hotel.util.BookingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserInternalService userInternalExtendExternalService;
    private final RoomInternalService roomInternalService;
    private final ServiceHotelInternalService serviceHotelInternalService;
    private final PromoCodeInternalService promoCodeInternalService;

    @Transactional
    public BookingDTO createBooking(BookingSimplDTO bookingSimplDTO) {
        PromoCode promoCode = null;
        try {
            Booking booking = BookingMapper.INSTANCE.bookingSimpleDTOToBooking(bookingSimplDTO);
            //    поиск и установление комнаты для бронирования
            Room room = findRoomForBooking(bookingSimplDTO);
            booking.setRoom(room);
            //    поиск и установление юзера из базы данных для бронирования
            User user = findUserForBooking(bookingSimplDTO);
            booking.setUser(user);
            //    поиск и установление сервисов для бронирования
            Set<ServiceHotel> serviceHotel = findAllServicesForBooking(bookingSimplDTO);
            booking.setServiceSet(serviceHotel);
            //    расчет количества дней проживания и стоимости сервисов для этого периода
            BigDecimal livingDays = BookingUtils.calculateLivingDays(bookingSimplDTO.getCheckInDate(), bookingSimplDTO.getCheckOutDate());
            BigDecimal totalPriceForServices = calculatePriceForServices(serviceHotel, livingDays);
            //     расчет стоимости за номер c сервисами без учета промокода
            BigDecimal totalPrice = calculateTtalPriceWithoutPromoCode(room, livingDays, totalPriceForServices);
            //     расчет стоимости за номер c сервисами c учетом промокода
            totalPrice = calculatePriceWithPromoCode(bookingSimplDTO, totalPrice);
            booking.setTotalPrice(totalPrice);
            booking.setStatusOfBooking(BookingStatus.CONFIRMED);

            return BookingMapper.INSTANCE.bookingToBookingDTO(bookingRepository.save(booking));
        } catch (RuntimeException ex) {
            log.error("Error проблемы с сохранением бронирования");
            throw ex;
        }
    }

    @Transactional
    public void deleteBookingById(Long id) {
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
    public List<BookingSimplDTO> findAllBookingsSimplDTOByRoomNumber(String roomNumber) {
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

    private Set<ServiceHotel> findAllServicesForBooking(BookingSimplDTO bookingSimplDTO) {
        Set<ServiceHotelSimpleDTO> serviceHotelDTOS = bookingSimplDTO.getServiceSet();
        if (serviceHotelDTOS.isEmpty()) {
            return new HashSet<>();
        }
        return serviceHotelDTOS.stream().map(serviceHotelSimpleDTO -> {
            return serviceHotelInternalService.findServiceHotelByTitle(serviceHotelSimpleDTO.getTitle());
        }).collect(Collectors.toSet());
    }

    private BigDecimal calculateTotalPriceWithPromoCode(
            BigDecimal totalPrice, PromoCode promoCode) {
        return switch (promoCode.getTypeOfPromoCode()) {
            case FIXED -> totalPrice.subtract(promoCode.getDiscountValue());
            case PERCENT -> totalPrice.multiply(BigDecimal.valueOf(100)
                    .divide(promoCode.getDiscountValue(),
                            2,
                            RoundingMode.HALF_UP));
        };
    }

    private Room findRoomForBooking(BookingSimplDTO bookingSimplDTO) {
        Room room = roomInternalService.findRoomByNumber(bookingSimplDTO.getRoomNumber());
        if (room.getIsAvailable() && !bookingRepository.isRoomAvailableForDates(room.getId()
                , bookingSimplDTO.getCheckInDate()
                , bookingSimplDTO.getCheckOutDate())) {
            throw new DataAccessException("комната не свободна на данные даты или ") {
            };
        }

        return room;
    }

    private User findUserForBooking(BookingSimplDTO bookingSimplDTO) {
        User user = userInternalExtendExternalService.findUserByLastNameAndFirstName(
                bookingSimplDTO.getUserSimpleDTO().getLastName(), bookingSimplDTO.getUserSimpleDTO().getFirstName());
        return user;
    }

    private BigDecimal calculatePriceForServices(Set<ServiceHotel> serviceHotels, BigDecimal livingDays) {
        if (!serviceHotels.isEmpty()) {
            return serviceHotels.stream().map(serviceHotelentity -> {
                return serviceHotelentity.getPrice().multiply(livingDays);
            }).reduce(BigDecimal.ZERO, (sum, price) -> sum.add(price));
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateTtalPriceWithoutPromoCode(Room room, BigDecimal livingDays, BigDecimal totalPriceForServices) {
        return room.getPricePerNight()
                .multiply(livingDays).add(totalPriceForServices);

    }

    private BigDecimal calculatePriceWithPromoCode(BookingSimplDTO bookingSimplDTO, BigDecimal totalPrice) {
        try {
            PromoCode promoCode = promoCodeInternalService.findActivePromoCodeByName(bookingSimplDTO.getPromoCodeDTO());
            return calculateTotalPriceWithPromoCode(totalPrice, promoCode);

        } catch (DataNotFoundException | DataAccessException ex) {
            log.error("Error промокод не найден {}", bookingSimplDTO.getPromoCodeDTO(), ex);
            return totalPrice;
        }

    }

}

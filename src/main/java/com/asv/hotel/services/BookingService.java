package com.asv.hotel.services;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.mapper.BookingMapper;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelSimpleDTO;
import com.asv.hotel.entities.*;
import com.asv.hotel.entities.enums.BookingStatus;
import com.asv.hotel.exceptions.DataNotFoundException;
import com.asv.hotel.repositories.BookingRepository;
import com.asv.hotel.repositories.PromoCodeRepository;
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
public class BookingService {

    private final PromoCodeRepository promoCodeRepository;

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final ServiceHotelService serviceHotelService;
    private final PromoCodeService promoCodeService;

    @Transactional
    public BookingDTO save(BookingSimplDTO bookingSimplDTO) {
        PromoCode promoCode = null;
        BigDecimal totalPrice = BigDecimal.ZERO;
        try {
            Room room = roomService.findByNumberReturnRoom(bookingSimplDTO.getRoomNumber());
            if (room.getIsAvailable() && !bookingRepository.isRoomAvailableForDates(room.getId()
                    , bookingSimplDTO.getCheckInDate()
                    , bookingSimplDTO.getCheckOutDate())) {
                throw new DataAccessException("комната не свободна на данные даты или ") {
                };
            }
            Booking booking = BookingMapper.INSTANCE.bookingSimpleDTOToBooking(bookingSimplDTO);
//            поиск юзера и возвращение сущности

            User user = userService.findUserByLastNameAndFirstNameReturnUser(
                    bookingSimplDTO.getUserSimpleDTO().getLastName(), bookingSimplDTO.getUserSimpleDTO().getFirstName());

//            внесенеие в бронирование
            booking.setUser(user);
            booking.setRoom(room);
            booking.setStatusOfBooking(BookingStatus.CONFIRMED);

            //расчет стоимости за номер для сервисов
            Set<ServiceHotel> serviceHotel = findAllServices(bookingSimplDTO.getServiceSet());
            BigDecimal livingDays = BookingUtils.calculateLivingDays(bookingSimplDTO.getCheckInDate(), bookingSimplDTO.getCheckOutDate());

            BigDecimal totalPriceForServices = BigDecimal.ZERO;

            if (!serviceHotel.isEmpty()) {
                totalPriceForServices = serviceHotel.stream().map(serviceHotelentity -> {
                    return serviceHotelentity.getPrice().multiply(livingDays);
                }).reduce(BigDecimal.ZERO, (sum, price) -> sum.add(price));
            }
            booking.setServiceSet(serviceHotel);


//            расчет стоимости за номер с промокодом
            totalPrice = room.getPricePerNight()
                    .multiply(livingDays).add(totalPriceForServices);
            try {
                promoCode = promoCodeService.findActivePromoCodeByName(bookingSimplDTO.getPromoCodeDTO());
                totalPrice = calculateTotalPriceWithPromoCode(totalPrice, promoCode);
                booking.setPromoCode(promoCode);
            } catch (DataNotFoundException | DataAccessException ex) {
                log.error("Error промокод не найден {}", bookingSimplDTO.getPromoCodeDTO(), ex);
            }
            booking.setTotalPrice(totalPrice);
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

    private Set<ServiceHotel> findAllServices(Set<ServiceHotelSimpleDTO> serviceHotelDTOS) {
        if (serviceHotelDTOS.isEmpty()) {
            return new HashSet<>();
        }
        return serviceHotelDTOS.stream().map(serviceHotelDTO -> {
            return serviceHotelService.findByTitleReturnEntity(serviceHotelDTO.getTitle());
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

}

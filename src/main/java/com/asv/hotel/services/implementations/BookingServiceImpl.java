package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.mapper.BookingMapper;
import com.asv.hotel.dto.roomdto.RoomSimpleDataBaseDTO;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelSimpleDTO;
import com.asv.hotel.entities.*;
import com.asv.hotel.entities.enums.BookingStatus;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import com.asv.hotel.repositories.BookingRepository;
import com.asv.hotel.services.*;
import com.asv.hotel.util.BookingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
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
    private final NotificationHotelService notificationHotelService;

    @Transactional
    public BookingDTO createBooking(BookingSimplDTO bookingSimplDTO) {
        Booking booking = BookingMapper.INSTANCE.bookingSimpleDTOToBooking(bookingSimplDTO);
        //    поиск и установление комнаты для бронирования
        Room room = findRoomForBooking(bookingSimplDTO);
        booking.setRoom(room);

        if(booking.getGuestList().size()>room.getCapacity()){
            log.warn("Warning: количество гостей при бронирование превышает возможности номера {}",
                    bookingSimplDTO.getRoomNumber());
            throw new HotelIncorrectInputData(String.format("Неверное количество гостей '%s' при бронирование комнаты '%s'",
                    booking.getGuestList().size(),
                    bookingSimplDTO.getRoomNumber() ));
        }
        //    поиск и установление юзера из базы данных для бронирования
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String nickname=userDetails.getUsername();
        User user=userInternalExtendExternalService.findUserByNickName(nickname);
        booking.setUser(user);

        //поиск и установление промокода
        PromoCode promoCode = promoCodeInternalService.findActivePromoCodeByName(bookingSimplDTO.getPromoCodeDTO());
        booking.setPromoCode(promoCode);
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
        Booking savedBooking = bookingRepository.save(booking);
        notificationHotelService.createNotificationBooking("Номер забронирован", savedBooking, "Бронирование номера");
        return BookingMapper.INSTANCE.bookingToBookingDTO(savedBooking);
    }

    @Transactional
    public void deleteBookingById(Long id) {
        if (bookingRepository.deleteBookingById(id) == 0) {
            log.error("Error данной брони не существует для удаления {}", id);
            throw new HotelDataNotFoundException("данной брони не существует для удаления");
        }
    }

    @Transactional
    public List<BookingSimplDTO> findAllBookingsSimplDTOByRoomNumber(String roomNumber) {
        List<Booking> bookingsList = bookingRepository.findAllByRoomNumber(roomNumber);
        if (bookingsList.isEmpty()) {
            log.error("лист с бронированиями пуст для данной комнаты {}", roomNumber);
            throw new HotelDataNotFoundException("лист с бронированиями пуст для данной комнаты");
        }
        return bookingsList.stream().map(b ->
                        BookingMapper.INSTANCE.bookingToBookingSimpleDTO(b))
                .toList();
    }

    @Transactional
    @Override
    public List<RoomSimpleDataBaseDTO> findRoomSimpleDTODataBaseByBookingDate(LocalDate checkInDate, LocalDate checkOutDate) {
        if (!checkInDate.isBefore(checkOutDate)) {
            log.error("Error:некорректные данные для поиска бронирования по датам заселение {} выселение {}",
                    checkInDate, checkOutDate);
            throw new HotelIncorrectInputData("Booking ", " Checking and Checkout dates");
        }

        return bookingRepository.findAllFreeRoomsBetweenDates(checkInDate, checkOutDate);
    }


    private Set<ServiceHotel> findAllServicesForBooking(BookingSimplDTO bookingSimplDTO) {
        Set<ServiceHotelSimpleDTO> serviceHotelDTOS = bookingSimplDTO.getServiceSet();
        if (serviceHotelDTOS.isEmpty()) {
            return Collections.emptySet();
        }
        return serviceHotelDTOS.stream().map(serviceHotelSimpleDTO -> {
            return serviceHotelInternalService.findServiceHotelByTitle(serviceHotelSimpleDTO.getTitle());
        }).collect(Collectors.toSet());
    }

    private BigDecimal calculateTotalPriceWithPromoCode(BigDecimal totalPrice, PromoCode promoCode) {
        if (promoCode == null) {
            return totalPrice;
        }
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
        if (room == null ||( Boolean.TRUE.equals(room.getIsAvailable()) && !bookingRepository.isRoomAvailableForDates(room.getId(),
                bookingSimplDTO.getCheckInDate(), bookingSimplDTO.getCheckOutDate()))) {
            throw new HotelDataNotFoundException("комната не свободна на данные даты или нет такой комнаты ") {
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
                        if (serviceHotelentity == null) {
                            return BigDecimal.ZERO;
                        }
                        return serviceHotelentity.getPrice().multiply(livingDays);
                    })
                    .reduce(BigDecimal.ZERO, (sum, price) -> sum.add(price));
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateTtalPriceWithoutPromoCode(Room room, BigDecimal livingDays, BigDecimal totalPriceForServices) {
        return room.getPricePerNight()
                .multiply(livingDays).add(totalPriceForServices);

    }

    private BigDecimal calculatePriceWithPromoCode(BookingSimplDTO bookingSimplDTO, BigDecimal totalPrice) {
        PromoCode promoCode = promoCodeInternalService.findActivePromoCodeByName(bookingSimplDTO.getPromoCodeDTO());
        return calculateTotalPriceWithPromoCode(totalPrice, promoCode);
    }

}

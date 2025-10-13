package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.bookingdto.ResponseBookingDTO;
import com.asv.hotel.dto.mapper.BookingMapper;
import com.asv.hotel.dto.roomdto.RoomSimpleDataBaseDTO;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelSimpleDTO;
import com.asv.hotel.entities.*;
import com.asv.hotel.entities.enums.BookingStatus;
import com.asv.hotel.entities.enums.UserRole;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import com.asv.hotel.repositories.BookingRepository;
import com.asv.hotel.security.util.JWTUtils;
import com.asv.hotel.services.*;
import com.asv.hotel.util.BookingUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapping;
import org.mapstruct.control.MappingControl;
import org.springframework.dao.DataAccessException;
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
    private final JWTUtils jwtUtils;

    @Transactional
    public BookingDTO createBooking(BookingSimplDTO bookingSimplDTO) {
        Booking booking = BookingMapper.INSTANCE.bookingSimpleDTOToBooking(bookingSimplDTO);
        //    поиск и установление комнаты для бронирования
        Room room = findRoomForBooking(bookingSimplDTO);
        booking.setRoom(room);
        //    поиск и установление юзера из базы данных для бронирования
        User user = findUserForBooking(bookingSimplDTO);
        booking.setUser(user);
        if (user == null) {
            log.warn("Error: не существует таких комнат {} и пользователей {} для бронирования в методе createBooking",
                    bookingSimplDTO.getUserSimpleDTO(), bookingSimplDTO.getRoomNumber());
            throw new HotelDataNotFoundException(
                    String.format("There is no this user '%s' and room '%s'",
                            bookingSimplDTO.getUserSimpleDTO(),
                            bookingSimplDTO.getRoomNumber()));
        }
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

    @Transactional
    public ResponseBookingDTO findBesponseBookingDTOByBookingId(Long id, HttpServletRequest request) {
        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            log.warn("Error: неверный номер брониования в методе findBesponseBookingDTOByBookingId id= {}", id);
            throw new HotelDataNotFoundException("нет такого номера бронирования");
        }

        if (!isCorrectRequest(booking, request)) {
            throw new HotelIncorrectInputData(" Неккоректный запрос для бронирвания ");
        }

        Set<ServiceHotelSimpleDTO> serviceHotelSimpleDTOS = booking.getServiceSet().stream()
                .map(serviceHotel ->
                        ServiceHoteMapper.INSTANCE.serviceHotelToServiceHotelSimpleDTO(serviceHotel))
                .collect(Collectors.toSet());

        return ResponseBookingDTO.builder()
                .bookingId(booking.getId())
                .statusOfBooking(booking.getStatusOfBooking())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .persons(booking.getPersons())
                .totalPrice(booking.getTotalPrice())
                .roomNumber(booking.getRoom().getNumber())
                .type(booking.getRoom().getType())
                .descriptionTypeOfRoom(booking.getRoom().getType().getDescription())
                .firstName(booking.getUser().getFirstName())
                .lastName(booking.getUser().getLastName())
                .email(booking.getUser().getEmail())
                .phoneNumber(booking.getUser().getPhoneNumber())
                .serviceHotelSimpleDTOS(serviceHotelSimpleDTOS)
                .build();
    }

    private boolean isCorrectRequest(Booking booking, HttpServletRequest request) {
        String authHeader = request.getHeader(JWTUtils.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(JWTUtils.BEARER)) {
            return Boolean.FALSE;
        }

        String accessToken = authHeader.substring(7);
        String nickNameFromToken = jwtUtils.extractUsername(accessToken);

        if (booking.getUser().getNickName().equals(nickNameFromToken)) {
            return Boolean.TRUE;
        }

        UserType userTypeFromTokenNickName = userInternalExtendExternalService.findUserTypeByUserNickName(nickNameFromToken);
        if (userTypeFromTokenNickName == null) {
            throw new HotelDataNotFoundException(String.format("неопознана роль из токена по никнейм '%s'",
                    nickNameFromToken));
        }

        if (!(userTypeFromTokenNickName.getRole().equals(UserRole.ADMIN) ||
                userTypeFromTokenNickName.getRole().equals(UserRole.MANAGER))) {
            throw new HotelIncorrectInputData(String.format("Неверные права доступа для поиска не своего бронирования " +
                    "nicknameToken '%s' bokingIdRequest '%s'", nickNameFromToken, booking));
        }

        return Boolean.TRUE;
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
        if (room == null || (Boolean.TRUE.equals(room.getIsAvailable()) && !bookingRepository.isRoomAvailableForDates(room.getId(),
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

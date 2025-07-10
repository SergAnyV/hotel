package com.asv.hotel.services;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimpleDTO;
import com.asv.hotel.dto.roomdto.RoomDTO;
import com.asv.hotel.dto.userdto.UserDTO;
import com.asv.hotel.dto.userdto.UserSimpleDTO;
import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.repositories.BookingRepository;
import com.asv.hotel.repositories.RoomRepository;
import com.asv.hotel.repositories.UserRepository;
import com.asv.hotel.repositories.UserTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)

class BookingServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private UserTypeDTO testUserTypeDTO;
    private UserTypeService userTypeService;
    private UserTypeRepository userTypeRepository;
    private UserDTO testUserDTO;
    private UserSimpleDTO testUserSimpleDTO;
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    private BookingDTO testBookingDTO;
    private RoomService roomService;
    private RoomRepository roomRepository;
    private RoomDTO testRoomDTO;

    public BookingServiceTest(UserService userService, UserRepository userRepository, UserTypeService userTypeService,
                              UserTypeRepository userTypeRepository, BookingRepository bookingRepository,
                              BookingService bookingService, RoomService roomService, RoomRepository roomRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userTypeService = userTypeService;
        this.userTypeRepository = userTypeRepository;
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    @BeforeEach
    void setUp() {
        testUserTypeDTO = UserTypeDTO.builder()
                .role("Client")
                .description("just a client")
                .isActive(true)
                .build();
        testUserDTO = UserDTO.builder()
                .role("Client")
                .email("ghi@j.hj")
                .firstName("Sergey")
                .fathersName("Vladimirivich")
                .lastName("Numm")
                .nickName("Best")
                .phoneNumber("86866")
                .password("1234")
                .build();
        testUserSimpleDTO = UserSimpleDTO.builder()
                .firstName("Sergey")
                .fathersName("Vladimirivich")
                .lastName("Numm")
                .phoneNumber("96")
                .build();
        testRoomDTO = RoomDTO.builder()
                .number("102")
                .type("Standard")
                .description("Test room")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(1000))
                .isAvailable(true)
                .build();
        System.out.println(userTypeService.save(testUserTypeDTO));
        System.out.println(userService.save(testUserDTO));
        System.out.println(roomService.save(testRoomDTO));
        testBookingDTO = BookingDTO.builder()
                .userSimpleDTO(testUserSimpleDTO)
                .checkInDate(LocalDate.parse("2023-12-20"))
                .checkOutDate(LocalDate.parse("2023-12-27"))
                .persons(2)
                .roomNumber("102")
                .build();

    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
        userTypeRepository.deleteAll();
    }

    @Test
    void createShouldeBeSavedBooking() {

        BookingSimpleDTO savedBookingDTO = bookingService.save(testBookingDTO);
        assertEquals(testBookingDTO.getUserSimpleDTO().getFirstName(), savedBookingDTO.getUserSimpleDTO().getFirstName());
    }
}
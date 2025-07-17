package com.asv.hotel.controllers;

import com.asv.hotel.dto.promocodedto.PromoCodeDTO;
import com.asv.hotel.dto.roomdto.RoomDTO;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.dto.userdto.UserDTO;
import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.entities.enums.RoomType;
import com.asv.hotel.entities.enums.TypeOfPromoCode;
import com.asv.hotel.repositories.*;
import com.asv.hotel.services.implementations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/db")
@RequiredArgsConstructor
@Tag(name = "DATABASE Management", description = "Заполняет базу данных по несколько позиций для каждого")
public class FillUpDataBasse {
    private final BookingServiceImpl bookingServiceImpl;
    private final PromoCodeServiceImpl promoCodeServiceImpl;
    private final RoomServiceImpl roomServiceImpl;
    private final ServiceHotelServiceImpl serviceHotelServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final UserTypeServiceImpl userTypeServiceImpl;
    private final UserRepository userRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final RoomRepository roomRepository;
    private final ServiceHotelRepository serviceHotelRepository;
    private final BookingRepository bookingRepository;

    @Operation(summary = "заполнение базы данных",
            description = "заполнение базы данных")
    @ApiResponse(responseCode = "204", description = " создан")
    @PostMapping
    @Transactional
    public ResponseEntity<Void> fillUpDB() {
        creatUserTypes();
        creatUsers();
        createPromoCodes();
        createRooms();
        createServices();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "удаление данных базы данных",
            description = "удаление данных  базы данных")
    @ApiResponse(responseCode = "204", description = " удалено")
    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> deleteAllDB() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        userTypeServiceImpl.deleteAllUserTypes();
        promoCodeRepository.deleteAll();
        roomRepository.deleteAll();
        serviceHotelRepository.deleteAll();

        return ResponseEntity.noContent().build();
    }


    private void creatUserTypes() {
        UserTypeDTO testUserTypeDTO = UserTypeDTO.builder()
                .role("Администратор")
                .description("просто админ")
                .isActive(true)
                .build();
        UserTypeDTO testUserTypeDTO2 = UserTypeDTO.builder()
                .role("Работник")
                .description("просто работник")
                .isActive(true)
                .build();
        UserTypeDTO testUserTypeDTO3 = UserTypeDTO.builder()
                .role("Клиент")
                .description("просто клиент")
                .isActive(true)
                .build();

        userTypeServiceImpl.createUserType(testUserTypeDTO);
        userTypeServiceImpl.createUserType(testUserTypeDTO2);
        userTypeServiceImpl.createUserType(testUserTypeDTO3);
    }

    private void creatUsers() {
        UserDTO testUserDTO = UserDTO.builder().role("Клиент").email("ghi@j.hj").firstName("Клиент")
                .fathersName("Владимирович").lastName("А").nickName("Фыва").phoneNumber("86866").password("1234").build();

        UserDTO testUserDTO2 = UserDTO.builder().role("Клиент").email("new@j.hj").firstName("Максим")
                .fathersName("Николаевич").lastName("Бугульма").nickName("BigBro").phoneNumber("8686643").password("123sfg344").build();
        UserDTO testUserDTO3 = UserDTO.builder().role("Администратор").email("newS@j.hj").firstName("Валентина")
                .fathersName("Максимовна").lastName("Бзик").nickName("BigSister").phoneNumber("89998686643").password("13f4234").build();
        UserDTO testUserDTO4 = UserDTO.builder().role("Администратор").email("neSS@j.hj").firstName("Елизавета")
                .fathersName("Артемовна").lastName("Бузиника").nickName("Sister").phoneNumber("89911643").password("19874").build();
        UserDTO testUserDTO5 = UserDTO.builder().role("Работник").email("nehg@j.hj").firstName("Денис")
                .fathersName("Николаевичч").lastName("Бузиника").nickName("Oseter").phoneNumber("8996432268").password("12342fs34").build();

        userServiceImpl.createUser(testUserDTO);
        userServiceImpl.createUser(testUserDTO2);
        userServiceImpl.createUser(testUserDTO3);
        userServiceImpl.createUser(testUserDTO4);
        userServiceImpl.createUser(testUserDTO5);
    }

    private void createPromoCodes() {

        PromoCodeDTO testPromoCodeDTO = PromoCodeDTO.builder().code("some")
                .typeOfPromoCode(TypeOfPromoCode.FIXED)
                .isActive(true)
                .validFromDate(LocalDate.of(2023, 7, 12))
                .validUntilDate(LocalDate.of(2024, 10, 23))
                .discountValue(new BigDecimal(32)).build();
        PromoCodeDTO testPromoCodeDTO2 = PromoCodeDTO.builder().code("newYear2024")
                .typeOfPromoCode(TypeOfPromoCode.FIXED)
                .isActive(true)
                .validFromDate(LocalDate.of(2023, 11, 15))
                .validUntilDate(LocalDate.of(2024, 1, 30))
                .discountValue(BigDecimal.valueOf(15)).build();
        PromoCodeDTO testPromoCodeDTO3 = PromoCodeDTO.builder().code("oldYear")
                .typeOfPromoCode(TypeOfPromoCode.PERCENT)
                .isActive(true)
                .validFromDate(LocalDate.of(2023, 11, 15))
                .validUntilDate(LocalDate.of(2025, 12, 30))
                .discountValue(BigDecimal.valueOf(10)).build();
        promoCodeServiceImpl.createPromoCode(testPromoCodeDTO);
        promoCodeServiceImpl.createPromoCode(testPromoCodeDTO2);
        promoCodeServiceImpl.createPromoCode(testPromoCodeDTO3);

    }

    private void createRooms() {
        RoomDTO testRoomDTO = RoomDTO.builder()
                .number("101")
                .type(RoomType.ECONOM)
                .description("Test room ECONOM")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(100))
                .isAvailable(true)
                .build();
        RoomDTO testRoomDTO2 = RoomDTO.builder()
                .number("102")
                .type(RoomType.STANDART)
                .description("Test room STANDART")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(500))
                .isAvailable(true)
                .build();
        RoomDTO testRoomDTO3 = RoomDTO.builder()
                .number("103")
                .type(RoomType.LUXE)
                .description("Test room LUXE")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(1200))
                .isAvailable(true)
                .build();
        RoomDTO testRoomDTO4 = RoomDTO.builder()
                .number("104")
                .type(RoomType.DELUXE)
                .description("Test room DELUXE")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(1500))
                .isAvailable(true)
                .build();
        RoomDTO testRoomDTO5 = RoomDTO.builder()
                .number("105")
                .type(RoomType.ECONOM)
                .description("Test room ECONOM недоступная")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(100))
                .isAvailable(false)
                .build();
        roomServiceImpl.createRoom(testRoomDTO);
        roomServiceImpl.createRoom(testRoomDTO2);
        roomServiceImpl.createRoom(testRoomDTO3);
        roomServiceImpl.createRoom(testRoomDTO4);
        roomServiceImpl.createRoom(testRoomDTO5);

    }

    private void createServices(){
        ServiceHotelDTO serviceHotelDTO=ServiceHotelDTO.builder()
                .title("Уборка")
                .description("уборка номера")
                .price(BigDecimal.valueOf(23))
                .build();
         ServiceHotelDTO serviceHotelDTO2=ServiceHotelDTO.builder()
                .title("Замена одноразовых принадлежностей")
                .description("Замена одноразовых принадлежностей")
                .price(BigDecimal.valueOf(10))
                .build();
        ServiceHotelDTO serviceHotelDTO3=ServiceHotelDTO.builder()
                .title("Завтрак в номер")
                .description("для тех кто готов доплатить")
                .price(BigDecimal.valueOf(25))
                .build();
          ServiceHotelDTO serviceHotelDTO4=ServiceHotelDTO.builder()
                .title("Прыжки на батуте")
                .description("для тех кто любит отдыхать по полной")
                .price(BigDecimal.valueOf(100))
                .build();
          serviceHotelServiceImpl.createServiceHotel(serviceHotelDTO);
          serviceHotelServiceImpl.createServiceHotel(serviceHotelDTO2);
          serviceHotelServiceImpl.createServiceHotel(serviceHotelDTO3);
          serviceHotelServiceImpl.createServiceHotel(serviceHotelDTO4);

    }


}

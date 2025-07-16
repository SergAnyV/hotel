package com.asv.hotel.services;

import com.asv.hotel.dto.roomdto.RoomDTO;
import com.asv.hotel.entities.enums.RoomType;
import com.asv.hotel.exceptions.DataAlreadyExistsException;
import com.asv.hotel.repositories.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RoomServiceTest {

    private DataSource dataSource;
    private RoomService roomService;
    private RoomRepository roomRepository;
    private RoomDTO testRoomDTO;

    public RoomServiceTest(RoomService roomService, RoomRepository roomRepository, DataSource dataSource) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
        this.dataSource = dataSource;
    }

    @BeforeEach
    void before() {
        testRoomDTO = RoomDTO.builder()
                .number("101")
                .type(RoomType.ECONOM)
                .description("Test room")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(1000))
                .isAvailable(true)
                .build();
    }

    @AfterEach
    void after(){
        try {
            roomRepository.deleteAll();
        }catch (Exception e){
          e.printStackTrace();
        }
    }

// проверка соединения с базой
    @Test
    void testDataSourceIsNotNull() {
        assertThat(dataSource).isNotNull();
    }

    //сохранение новой комнаты в репозитории CREATE
    @Test
    void createRoomRoomShouldBeSavedRoom() {
        RoomDTO roomDTO = roomService.createRoom(testRoomDTO);
        assertEquals(roomDTO.getNumber(), testRoomDTO.getNumber());
        assertEquals(roomDTO.getType(),testRoomDTO.getType());
        assertEquals(roomDTO.getPricePerNight(),testRoomDTO.getPricePerNight());
        assertEquals(roomDTO.getIsAvailable(),testRoomDTO.getIsAvailable());
    }

    @Test
    void createRoomRoom_ShouldThrowIfRoomExists() {
        roomService.createRoom(testRoomDTO);

        assertThatThrownBy(() -> roomService.createRoom(testRoomDTO))
                .isInstanceOf(DataAlreadyExistsException.class)
                .hasMessageContaining(testRoomDTO.getNumber());
    }
    // READ чтение из базф
    @Test
    void findAll_ShouldReturnAllRoomsRooms() {
        roomService.createRoom(testRoomDTO);
        RoomDTO secondRoomDTO= RoomDTO.builder()
                .number("102")
                .type(RoomType.DELUXE)
                .description("Test room2")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(1000))
                .isAvailable(true)
                .build();
        roomService.createRoom(secondRoomDTO);
        List<RoomDTO> rooms = roomService.findAllRoomsDTO();

        assertEquals(rooms.size(),2);
    }

    @Test
    void findRoomDTOByNumber_ShouldReturnRoom() {
        roomService.createRoom(testRoomDTO);
        RoomDTO foundRoom = roomService.findRoomDTOByNumber("101");

        assertThat(foundRoom).isNotNull();
        assertThat(foundRoom.getNumber()).isEqualTo("101");
    }

    @Test
    void findRoomDTOByNumber_ShouldThrowIfNotFound() {
        assertThatThrownBy(() -> roomService.findRoomDTOByNumber("999"));
    }
    // UPDATE обновление
    @Test
    void updateRoom_ShouldChangeDataRoomExistingRoom() {
        RoomDTO savedRoom = roomService.createRoom(testRoomDTO);
        savedRoom.setType(RoomType.DELUXE);
        savedRoom.setPricePerNight(BigDecimal.valueOf(1500));

        RoomDTO updatedRoom = roomService.changeDataRoom(savedRoom);

        assertThat(updatedRoom.getType()).isEqualTo(RoomType.DELUXE);
        assertThat(updatedRoom.getPricePerNight()).isEqualByComparingTo("1500");
    }

    // DELETE
    @Test
    void deleteRoomByNumberRoom_ShouldRemoveRoom() {
        roomService.createRoom(testRoomDTO);
        roomService.deleteRoomByNumber("101");

        assertThat(roomRepository.findRoomByNumberLikeIgnoreCase("101")).isEmpty();
    }
}
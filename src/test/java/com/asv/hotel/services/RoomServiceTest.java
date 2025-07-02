package com.asv.hotel.services;

import com.asv.hotel.dto.RoomDTO;
import com.asv.hotel.exceptions.rooms.DataAlreadyExistsException;
import com.asv.hotel.exceptions.rooms.DataNotFoundException;
import com.asv.hotel.repositories.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                .type("Standard")
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
    void saveRoomShouldBeSavedRoom() {
        RoomDTO roomDTO = roomService.save(testRoomDTO);
        assertEquals(roomDTO.getNumber(), testRoomDTO.getNumber());
        assertEquals(roomDTO.getType(),testRoomDTO.getType());
        assertEquals(roomDTO.getPricePerNight(),testRoomDTO.getPricePerNight());
        assertEquals(roomDTO.getIsAvailable(),testRoomDTO.getIsAvailable());
    }

    @Test
    void saveRoom_ShouldThrowIfRoomExists() {
        roomService.save(testRoomDTO);

        assertThatThrownBy(() -> roomService.save(testRoomDTO))
                .isInstanceOf(DataAlreadyExistsException.class)
                .hasMessageContaining(testRoomDTO.getNumber());
    }
    // READ чтение из базф
    @Test
    void findAll_ShouldReturnAllRooms() {
        roomService.save(testRoomDTO);
        RoomDTO secondRoomDTO= RoomDTO.builder()
                .number("102")
                .type("Economy")
                .description("Test room2")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(1000))
                .isAvailable(true)
                .build();
        roomService.save(secondRoomDTO);
        List<RoomDTO> rooms = roomService.findAll();

        assertEquals(rooms.size(),2);
    }

    @Test
    void findByNumber_ShouldReturnRoom() {
        roomService.save(testRoomDTO);
        RoomDTO foundRoom = roomService.findByNumber("101");

        assertThat(foundRoom).isNotNull();
        assertThat(foundRoom.getNumber()).isEqualTo("101");
    }

    @Test
    void findByNumber_ShouldThrowIfNotFound() {
        assertThatThrownBy(() -> roomService.findByNumber("999"));
    }
    // UPDATE обновление
    @Test
    void updateRoom_ShouldUpdateExistingRoom() {
        RoomDTO savedRoom = roomService.save(testRoomDTO);
        savedRoom.setType("Deluxe");
        savedRoom.setPricePerNight(BigDecimal.valueOf(1500));

        RoomDTO updatedRoom = roomService.update(savedRoom);

        assertThat(updatedRoom.getType()).isEqualTo("Deluxe");
        assertThat(updatedRoom.getPricePerNight()).isEqualByComparingTo("1500");
    }

    // DELETE
    @Test
    void deleteRoom_ShouldRemoveRoom() {
        roomService.save(testRoomDTO);
        roomService.delete("101");

        assertThat(roomRepository.findRoomByNumberLikeIgnoreCase("101")).isEmpty();
    }
}
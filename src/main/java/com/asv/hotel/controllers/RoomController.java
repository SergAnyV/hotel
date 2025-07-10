package com.asv.hotel.controllers;

import com.asv.hotel.dto.roomdto.RoomDTO;
import com.asv.hotel.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "API для управления номерами отеля")
public class RoomController {
    private final RoomService roomService;


    @Operation(summary = "Получить все номера",
            description = "Возвращает список всех номеров отеля")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }


    @Operation(summary = "Найти номер по номеру",
            description = "Возвращает данные номера по номеру комнаты")
    @ApiResponse(responseCode = "200", description = "Номер найден")
    @ApiResponse(responseCode = "404", description = "Номер не найден")
    @GetMapping("/{number}")
    public ResponseEntity<RoomDTO> getRoomByNumber(@PathVariable String number) {

        RoomDTO roomDTO = roomService.findByNumber(number);
        return ResponseEntity.ok(roomDTO);

    }


    @Operation(summary = "Создать новый номер",
            description = "создает новый номер")
    @ApiResponse(responseCode = "201", description = "Номер создан")
    @ApiResponse(responseCode = "409", description = "Номер не создан")
    @PostMapping
    public ResponseEntity<RoomDTO> createRoom( @RequestBody @Valid RoomDTO roomDTO) {
        RoomDTO newRoomDTO = roomService.save(roomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoomDTO);
    }

    @Operation(summary = "Обновить данные номера",
            description = "обновляет данные существующего номера")
    @ApiResponse(responseCode = "200", description = "Номер обновлен")
    @ApiResponse(responseCode = "404", description = "Номер не найден")
    @ApiResponse(responseCode = "409", description = "Конфликт данных")
    @PutMapping
    public ResponseEntity<RoomDTO> updateRoom(
            @RequestBody @Valid RoomDTO roomDTO) {
        RoomDTO updatedRoom = roomService.update(roomDTO);
        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(summary = "Удалить номер",
            description = "удвляет данные существующего номера")
    @ApiResponse(responseCode = "204", description = "Номер удален")
    @ApiResponse(responseCode = "404", description = "Номер не найден")
    @DeleteMapping("/{number}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String number) {
        roomService.delete(number);
        return ResponseEntity.noContent().build();
    }

}

package com.asv.hotel.controllers;


import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.roomdto.RoomSimpleDTO;
import com.asv.hotel.dto.roomdto.RoomSimpleDTODataBase;
import com.asv.hotel.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "REST API для управления бронирования номерами отеля")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Создать новое бронирование",
            description = "создает новое бронирование")
    @ApiResponse(responseCode = "201", description = "бронирование создано")
    @ApiResponse(responseCode = "409", description = "бронирование не создано")
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingSimplDTO bookingSimplDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(bookingSimplDTO));
    }


    @Operation(summary = "Удалить бронирование",
            description = "удаляет данные существующего бронирования по id")
    @ApiResponse(responseCode = "204", description = "бронирование удалено")
    @ApiResponse(responseCode = "404", description = "бронирование не найдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable
            @NotNull
            Long id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Найти бронирование по номеру комнаты",
            description = "Возвращает данные бронирования по номеру комнаты")
    @ApiResponse(responseCode = "200", description = "бронирования найдены")
    @ApiResponse(responseCode = "404", description = "бронирования не найдены")

    @GetMapping("/{number}")
    public ResponseEntity<List<BookingSimplDTO>> getAllBookingsByRoomNumber(
            @PathVariable
            @NotBlank(message = "номер комнаты не должен быть пустым")
            @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9]+$", message = "Комната может содержать только буквы, цифры ")
            String number) {
        return ResponseEntity.ok(bookingService.findAllBookingsSimplDTOByRoomNumber(number));
    }


    @Operation(summary = "Найти свободные комнаты для входных дат проживания",
            description = "Возвращает список содержащий номер комнаты, тип комнаты, " +
                    "возможнок количество проживающих для комнаты, описание комнаты , " +
                    "цену за сутки в данной комнате")
    @ApiResponse(responseCode = "200", description = "список комнат")
    @ApiResponse(responseCode = "404", description = "нет свободных комнат в заданные даты")
    @ApiResponse(responseCode = "400", description = "указаны некорректные данные для поиска")
    @GetMapping("/freeroom")
    public ResponseEntity<List<RoomSimpleDTODataBase>> getFreeRoomByDate(@RequestParam
                                                                 @FutureOrPresent(message = "Дата начала проживания " +
                                                                         "должна быть сегодня или позже")
                                                                 @NotNull(message = "Дата начала проживания " +
                                                                         "обязательна")
                                                                 LocalDate checkInDate,

                                                                         @RequestParam
                                                                 @Future(message = "Дата окончания проживания должна " +
                                                                         "быть в будущем")
                                                                 @NotNull(message = "Дата окончания проживания " +
                                                                         "обязательна")
                                                                 LocalDate checkOutDate) {
        return ResponseEntity.ok(bookingService.findRoomSimpleDTOByBookingDate(checkInDate, checkOutDate));
    }


}

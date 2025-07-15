package com.asv.hotel.controllers;


import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.services.BookingService;
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
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "API для управления бронирования номерами отеля")
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Создать новое бонирование",
            description = "создает новый бонирование")
    @ApiResponse(responseCode = "201", description = "бонирование создано")
    @ApiResponse(responseCode = "409", description = "бонирование не создано")
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingSimplDTO bookingSimplDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.save(bookingSimplDTO));
    }

    @Operation(summary = "Удалить бонирование",
            description = "удаляет данные существующего бонирование по id")
    @ApiResponse(responseCode = "204", description = "бонирование удалено")
    @ApiResponse(responseCode = "404", description = "бонирование не найдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Найти бонирование по номеру комнаты",
            description = "Возвращает данные бонирования по номеру комнаты")
    @ApiResponse(responseCode = "200", description = "бонирование найдены")
    @ApiResponse(responseCode = "404", description = "бонирование не найдены")
    @GetMapping("/{number}")
    public ResponseEntity<List<BookingSimplDTO>> getAllBookingsByRoomNumber9(@PathVariable String number) {
        return ResponseEntity.ok(bookingService.findAllByRoomNumber(number));
    }
}

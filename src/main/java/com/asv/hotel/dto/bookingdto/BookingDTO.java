package com.asv.hotel.dto.bookingdto;

import com.asv.hotel.dto.roomdto.RoomSimpleDTO;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.dto.userdto.UserSimpleDTO;
import com.asv.hotel.entities.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class BookingDTO {

    @Schema(description = "уникальный номер бронирования", example = "1L")
    @NotNull(message = "уникальный номер бронирования обязателен")
    private Long id;

    @Schema(description = "Дата заезда", example = "2023-06-01")
    @NotNull(message = "Дата начала проживания обязательна")
    @FutureOrPresent
    private LocalDate checkInDate;

    @Schema(description = "Дата окончания действия", example = "2023-07-01")
    @NotNull(message = "Дата выезда обязательна")
    @Future
    private LocalDate checkOutDate;

    @Schema(description = "количество человек", example = "2", type = "integer")
    @Min(value = 1, message = "Вместимость должна быть не менее 1")
    @Max(value = 10, message = "Вместимость должна быть не более 10")
    private Integer persons;

    @Schema(description = "общая сумма не должна быть 0 или отрицательная ", example = "1000")
    @Positive(message = "цена должна быть положительна")
    private BigDecimal totalPrice;

    @Schema(description = "статус (CONFIRMED и т.д.)",allowableValues = {"REQUEST", "CONFIRMED","CANCELLED"})
    @NotNull
    private BookingStatus statusOfBooking;

    @Schema(description = "Дата создания записи", example = "2023-01-01T12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent
    private LocalDate createdAt;

    @Schema(description = "Данные комнаты")
    private RoomSimpleDTO roomSimpleDTO;

    @Schema(description = "Данные пользователя")
    private UserSimpleDTO userSimpleDTO;

    @Schema(description = "Название промокода")
    private String promoCodeDTO;

    @Schema(description = "подключенные сервисы")
    private Set<ServiceHotelDTO> serviceHotelDTOS ;
}

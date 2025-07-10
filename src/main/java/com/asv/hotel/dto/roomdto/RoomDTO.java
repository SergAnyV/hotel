package com.asv.hotel.dto.roomdto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(name = "Модель данных номера отеля", description = "модель передачи данных через JSON")
public class RoomDTO {

    @Schema(description = "номер комнаты", example = "101")
    @NotBlank(message = "номер комнты не должен быть пустым")
    private String number;

    @Schema(description = "Тип комнаты (стандарт, люкс и т.д.)", example = "стандарт")
    @NotBlank(message = "тип комнты не должен быть пустым")
    @Size(min = 3,max = 30,message = "количество символов 3-30")
    private String type;

    @Schema(description = "Описание комнаты и удобств", example = "Номер с видом на море")
    @NotBlank(message = "Описание комнты не должен быть пустым")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    private String description;

    @Schema(description = "Вместимость (количество человек)", example = "2", type = "integer")
    @Min(value = 1, message = "Вместимость должна быть не менее 1")
    @Max(value = 10, message = "Вместимость должна быть не менее 10")
    private Integer capacity;

    @Schema(description = "Цена за ночь", example = "1500.00")
    @Positive(message = "цена должна быть положительна")
    private BigDecimal pricePerNight;

    @Schema(description = "Доступность номера для бронирования", example = "true")
    @NotNull(message = "Поле isAvailable обязательно")
    private Boolean isAvailable;

    @Schema(description = "Дата создания записи", example = "2023-01-01T12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления", example = "2023-01-02T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @FutureOrPresent
    private LocalDateTime updatedAt;

}

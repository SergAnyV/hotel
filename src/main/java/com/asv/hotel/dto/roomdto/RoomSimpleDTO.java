package com.asv.hotel.dto.roomdto;

import com.asv.hotel.entities.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomSimpleDTO {
    @Schema(description = "номер комнаты", example = "101")
    @NotBlank(message = "номер комнты не должен быть пустым")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9]+$", message = "Комната может содержать только буквы, цифры ")
    private String number;

    @Schema(description = "Тип комнаты (стандарт, люкс и т.д.)", allowableValues = {"ECONOM", "STANDART", "LUXE", "DELUXE"})
    @NotBlank(message = "тип комнаты не должен быть пустым")
    private RoomType type;

    @Schema(description = "Описание комнаты и удобств", example = "Номер с видом на море")
    @NotBlank(message = "Описание комнаты не должен быть пустым")
    @NotBlank(message = " не должен быть пустым")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    private String description;

    @Schema(description = "Вместимость (количество человек)", example = "2", type = "integer")
    @Min(value = 1, message = "Вместимость должна быть не менее 1")
    @Max(value = 10, message = "Вместимость должна быть не менее 10")
    private Integer capacity;

    @Schema(description = "Цена за ночь", example = "1500.00")
    @Positive(message = "цена должна быть положительна")
    private BigDecimal pricePerNight;

}

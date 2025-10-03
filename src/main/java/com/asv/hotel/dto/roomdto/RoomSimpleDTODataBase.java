package com.asv.hotel.dto.roomdto;

import com.asv.hotel.entities.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomSimpleDTODataBase {
    @Schema(description = "номер комнаты", example = "101")
    private String number;

    @Schema(description = "Тип комнаты (стандарт, люкс и т.д.)", allowableValues = {"ECONOM", "STANDART", "LUXE", "DELUXE"})
    private String type;

    @Schema(description = "Описание комнаты и удобств", example = "Номер с видом на море")
    private String description;

    @Schema(description = "Вместимость (количество человек)", example = "2", type = "integer")
    private Integer capacity;

    @Schema(description = "Цена за ночь", example = "1500.00")
    private BigDecimal pricePerNight;
}

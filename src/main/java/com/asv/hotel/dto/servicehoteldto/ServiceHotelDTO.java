package com.asv.hotel.dto.servicehoteldto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "Модель данных сервиса отеля", description = "модель передачи данных через JSON")
public class ServiceHotelDTO {

    @Schema(description = "название сервиса", example = "уборка номера")
    @NotBlank(message = " не должен быть пустым")
    private String title;

    @Schema(description = "описание сервиса", example = "уборка в номере и замеа одноразовых принадлежностей")
    @NotBlank(message = " не должен быть пустым")
    private String description;

    @Schema(description = "Цена за сервис в сутки", example = "150.00")
    @Positive(message = "цена должна быть положительна")
    private BigDecimal price;
}

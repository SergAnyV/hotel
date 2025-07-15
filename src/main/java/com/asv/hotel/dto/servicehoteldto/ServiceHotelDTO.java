package com.asv.hotel.dto.servicehoteldto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "Модель данных сервиса отеля", description = "модель передачи данных через JSON")
public class ServiceHotelDTO {

    @Schema(description = "название сервиса", example = "Уборка")
    @NotBlank(message = " не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    private String title;

    @Schema(description = "описание сервиса", example = "уборка номера")
    @NotBlank(message = " не должен быть пустым")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    private String description;

    @Schema(description = "Цена за сервис в сутки,три знака перед. и два после", example = "150.00")
    @Positive(message = "цена должна быть положительна")
    @NotNull
    private BigDecimal price;
}

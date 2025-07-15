package com.asv.hotel.dto.servicehoteldto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "ServiceHotelSimpleDTO", description = "Упрощенная модель сервиса отеля без цены")
public class ServiceHotelSimpleDTO {
    @Schema(description = "название сервиса", example = "Уборка")
    @NotBlank(message = " не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    private String title;

    @Schema(description = "описание сервиса", example = "уборка номера")
    @NotBlank(message = " не должен быть пустым")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    private String description;
}

package com.asv.hotel.dto.promocodedto;

import com.asv.hotel.entities.enums.TypeOfPromoCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@Schema(name = "Модель данных промокода", description = "модель передачи данных через JSON")
public class PromoCodeDTO {

    @Schema(description = "промокод", example = "Халява")
    @NotBlank(message = "промокод не должен быть пустым")
    @Size(max = 20, message = "Длина промокода не должна превышать 20 символов")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9]+$", message = "Промокод может содержать только буквы, цифры ")
    private String code;

    @Schema(description = "Тип промокода", example = "FIXED", allowableValues = {"FIXED", "PERCENT"})
    @NotNull(message = "Тип промокода обязателен")
    private TypeOfPromoCode typeOfPromoCode;

    @Schema(description = "Значение скидки", example = "10.00")
    @NotNull(message = "Значение скидки обязательно")
    private BigDecimal discountValue;

    @Schema(description = "Дата начала действия", example = "2023-06-01")
    @NotNull(message = "Дата начала действия обязательна")
    @FutureOrPresent
    private LocalDate validFromDate;

    @Schema(description = "Дата окончания действия", example = "2023-07-01")
    @NotNull(message = "Дата окончания действия обязательна")
    @FutureOrPresent
    private LocalDate validUntilDate;

    @Schema(description = "Активен ли промокод true/false",
            example = "true",
            defaultValue = "true",
            allowableValues = {"true", "false"} )
    @NotNull(message = "активен или нет обязательно")
    private Boolean isActive;

}

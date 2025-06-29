package com.asv.hotel.dto;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RoomDTO {
//    @NotBlank(message = "номер комнты не должен быть пустым")
    private String number;
//    @NotBlank(message = "тип комнты не должен быть пустым")
    private String type;
    private String description;
//    @Min(value = 1, message = "Вместимость должна быть не менее 1")
    private Integer capacity;
//    @Positive(message = "цена должна быть положительна")
    private BigDecimal pricePerNight;
    private Boolean isAvailable;
//    @PastOrPresent
    private LocalDateTime createdAt;
//    @FutureOrPresent
    private LocalDateTime updatedAt;

}

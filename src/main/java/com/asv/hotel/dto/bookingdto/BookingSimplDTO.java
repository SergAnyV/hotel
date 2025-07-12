package com.asv.hotel.dto.bookingdto;


import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.dto.userdto.UserSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Schema(description = "Модель данных бронирования  с описание характеристик" +
        ",модель передачи данных через JSON")
public class BookingSimplDTO {

    @Schema(description = "Дата заезда", example = "2023-06-01")
    @NotNull(message = "Дата начала проживания обязательна")
    private LocalDate checkInDate;

    @Schema(description = "Дата окончания действия", example = "2023-07-01")
    @NotNull(message = "Дата выезда обязательна")
    private LocalDate checkOutDate;

    @Schema(description = "количество человек", example = "2", type = "integer")
    @Min(value = 1, message = "Вместимость должна быть не менее 1")
    @Max(value = 10, message = "Вместимость должна быть не более 10")
    private Integer persons;

    @Schema(description = "номер комнаты", example = "101")
    @NotBlank(message = "номер комнты не должен быть пустым")
    private String roomNumber;

    @Schema(description = "Описание того кто зачселяется")
    @NotNull(message = "Описание не должен быть пустым")
    private UserSimpleDTO userSimpleDTO;

    @Schema(description = "промокод")
    private String promoCodeDTO;

    @Schema(description = "сервисы")
    private Set<ServiceHotelDTO> serviceSet;

}

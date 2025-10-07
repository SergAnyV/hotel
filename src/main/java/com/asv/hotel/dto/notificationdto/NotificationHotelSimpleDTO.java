package com.asv.hotel.dto.notificationdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class NotificationHotelSimpleDTO {

    @Schema(description = "сообщение предаваемое в уведомлении", example = "номер забронирован")
    @NotBlank(message = "сообщение не должно быть пустым быть пустым")
    @Size(min = 3, max = 100, message = "количество символов 3-100")
    private String message;

    @Schema(description = "никнайм пользователя", example = "BigBro")
    @NotBlank(message = "никнайм пользователя, не должен быть пустым")
    @Size(min = 3, max = 20, message = "количество символов 3-20")
    private String nickName;

    @Schema(description = "уникальный номер бронирования", example = "1L")
    @NotNull(message = "уникальный номер бронирования обязателен")
    private Long bookingId;
}

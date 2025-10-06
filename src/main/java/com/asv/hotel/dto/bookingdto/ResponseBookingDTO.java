package com.asv.hotel.dto.bookingdto;

import com.asv.hotel.dto.servicehoteldto.ServiceHotelSimpleDTO;
import com.asv.hotel.entities.enums.BookingStatus;
import com.asv.hotel.entities.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Информативная модель данных по бронированию возвращаемая пользователю при завпросе ")
@Builder
@Getter
public class ResponseBookingDTO {

    @Schema(description = "Уникальный номер бронирования")
    private Long bookingId;

    @Schema(description = "дата начала бронирования")
    private LocalDate checkInDate;

    @Schema(description = "дата окончания бронирования")
    private LocalDate checkOutDate;

    @Schema(description = "количество проживающих")
    private Integer persons;

    @Schema(description = "полная стоимость проживания")
    private BigDecimal totalPrice;

    @Schema(description = "статус бронирования")
    private BookingStatus statusOfBooking;

    @Schema(description = "номер комнаты")
    private String roomNumber;

    @Schema(description = "тип комнаты")
    private RoomType type;

    @Schema(description = "пояснение типа комнаты")
    private String descriptionTypeOfRoom;

    @Schema(description = "Имя пользователя на кого создана бронь")
    private String firstName;

    @Schema(description = "Фамилия пользователя на кого создана бронь")
    private String lastName;

    @Schema(description = "Email пользователя на кого создана бронь")
    private String email;

    @Schema(description = "Номер телефона пользователя на кого создана бронь")
    private String phoneNumber;

    private Set<ServiceHotelSimpleDTO> serviceHotelSimpleDTOS;
}

package com.asv.hotel.dto.notificationdto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationHotelDto {

    private String message;

    private LocalDateTime createdAt;

    private String nickName;

    private Long bookingId;

}

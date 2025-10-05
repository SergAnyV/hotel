package com.asv.hotel.dto.notificationdto;

import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.dto.userdto.UserSimpleDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private String message;

    private LocalDateTime createdAt;

    private String nickName;

    private Long bookingId;

}

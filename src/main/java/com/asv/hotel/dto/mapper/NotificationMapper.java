package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.notificationdto.NotificationDto;
import com.asv.hotel.entities.NotificationHotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, BookingMapper.class})
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "nickName",source = "user.nickName")
    @Mapping(target = "bookingId",source = "booking.id")
    NotificationDto notificationToNotificationDTO(NotificationHotel notificationHotel);

    @Mapping(target = "message",source = "message")
    @Mapping(target = "createdAt",source = "createdAt")
    @Mapping(target = "createdAt",source = "createdAt")
    NotificationHotel notificationdtoToNotification(NotificationDto notificationDto);
}

package com.asv.hotel.services;

import com.asv.hotel.dto.notificationdto.NotificationDto;
import com.asv.hotel.entities.Booking;

public interface NotificationHotelService {
    NotificationDto createNotificationBooking(String message, Booking booking, String subject);
}

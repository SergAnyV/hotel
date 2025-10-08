package com.asv.hotel.services;

import com.asv.hotel.dto.notificationdto.NotificationHotelDto;
import com.asv.hotel.entities.Booking;

public interface NotificationHotelService {
    NotificationHotelDto createNotificationBooking(String message, Booking booking, String subject);
}

package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.mapper.NotificationMapper;
import com.asv.hotel.dto.notificationdto.NotificationDto;
import com.asv.hotel.entities.Booking;
import com.asv.hotel.entities.NotificationHotel;
import com.asv.hotel.entities.User;
import com.asv.hotel.repositories.NotificationRepository;
import com.asv.hotel.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Transactional
    public NotificationDto createNotification(String message, User user, Booking booking,String subject) {

        NotificationHotel notificationHotel = NotificationHotel.builder()
                .message(message)
                .user(user)
                .booking(booking)
                .build();

        notificationHotel = notificationRepository.save(notificationHotel);
        emailService.sendNotificationEmail(
                user.getEmail(),
                subject,
                message
        );

        return NotificationMapper.INSTANCE.notificationToNotificationDTO(notificationHotel);
    }

}

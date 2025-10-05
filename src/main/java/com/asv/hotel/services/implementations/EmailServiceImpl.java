package com.asv.hotel.services.implementations;

import com.asv.hotel.services.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmailServiceImpl implements EmailService {


    @Override
    public void sendNotificationEmail(String to, String subject, String message) {

    }
}

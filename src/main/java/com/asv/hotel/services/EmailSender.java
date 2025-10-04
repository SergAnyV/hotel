package com.asv.hotel.services;

import java.util.List;

public interface EmailSender {
    void sendEmail(String messsage, List<String> recipient);
}

package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import org.springframework.http.HttpStatus;

public class HotelDataNotFoundException extends HotelMainException {
    public HotelDataNotFoundException(String message) {
        super(new ErrorMessage(HttpStatus.NOT_FOUND, message));
    }

    public HotelDataNotFoundException(String resourceType, String identifier) {
        super(new ErrorMessage(
                HttpStatus.NOT_FOUND,
                String.format("%s not found with identifier: %s", resourceType, identifier)
        ));
    }
    public HotelDataNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}




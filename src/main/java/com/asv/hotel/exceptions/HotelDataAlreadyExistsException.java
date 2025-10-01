package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import org.springframework.http.HttpStatus;

public class HotelDataAlreadyExistsException extends HotelMainException {
    public HotelDataAlreadyExistsException(String message) {
        super(new ErrorMessage(HttpStatus.CONFLICT, message));
    }

    public HotelDataAlreadyExistsException(String resourceType, String identifier) {
        super(new ErrorMessage(
                HttpStatus.CONFLICT,
                String.format("%s already exists with identifier: %s", resourceType, identifier)
        ));
    }

    public HotelDataAlreadyExistsException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}

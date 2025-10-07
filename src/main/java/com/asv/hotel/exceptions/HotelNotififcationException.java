package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import org.springframework.http.HttpStatus;

public class HotelNotififcationException extends HotelMainException {
    public HotelNotififcationException(String message) {
        super(new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,message
        ));
    }

    public HotelNotififcationException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public HotelNotififcationException(String resource, String idntifier) {
        super(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("%s Notification Error %s ", resource, idntifier))
        );
    }
}

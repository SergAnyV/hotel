package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import org.springframework.http.HttpStatus;

public class HotelIncorrectInputData extends HotelMainException {
    public HotelIncorrectInputData(String message) {
        super(
                new ErrorMessage(HttpStatus.BAD_REQUEST, message)
        );
    }

    public HotelIncorrectInputData(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public HotelIncorrectInputData(String resource, String idntifier) {
        super(new ErrorMessage(HttpStatus.BAD_REQUEST,
                String.format("%s Incorrect data format %s ", resource, idntifier))
        );
    }
}

package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import org.springframework.http.HttpStatus;

public class HotelMainException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public HotelMainException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
    public HotelMainException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return errorMessage.getHttpStatus();
    }

}

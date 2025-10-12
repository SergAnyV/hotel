package com.asv.hotel.exceptions;


import com.asv.hotel.dto.ErrorMessage;
import org.springframework.http.HttpStatus;

public class HotelAuthenticationException extends HotelMainException {
        public HotelAuthenticationException(String message) {
                super(new ErrorMessage(HttpStatus.UNAUTHORIZED, message));
        }

        public HotelAuthenticationException(ErrorMessage errorMessage) {
                super(errorMessage);
        }
}

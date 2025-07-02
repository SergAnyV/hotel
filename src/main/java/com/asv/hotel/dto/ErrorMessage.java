package com.asv.hotel.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorMessage {
    private HttpStatus httpStatus;
    private String message;

}

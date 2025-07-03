package com.asv.hotel.exceptions.rooms;


import lombok.Getter;

@Getter
public class ErrorAnswer {
    private String error;
    private String message;


    public ErrorAnswer(String error, String message) {
        this.error = error;
        this.message = message;

    }
}

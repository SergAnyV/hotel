package com.asv.hotel.exceptions.rooms;

public class RoomAlreadyExistsException extends RuntimeException{
    public RoomAlreadyExistsException(String number) {
        super("данная комната уже существует  " + number);
    }
}

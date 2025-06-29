package com.asv.hotel.exceptions.rooms;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String number) {
        super("нет комнаты с номером " + number);
    }
}




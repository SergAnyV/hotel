package com.asv.hotel.entities.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookingStatus {
    REQUEST(" запрос на подтверждение брони"),
    CONFIRMED(" бронирование подтвреждено"),
    CANCELLED("бронирование отменено");


    private final String descriptio;
}

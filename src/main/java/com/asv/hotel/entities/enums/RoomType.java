package com.asv.hotel.entities.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoomType {
    ECONOM("обычный номер чтобы переночевать"),
    STANDART("улучшенный номер с небольшими улучшениями"),
    LUXE("повышенного комфорта "),
    DELUXE("комфорт уровня 'все сделаем за вас не переживайте' ");
    private final String description;
}

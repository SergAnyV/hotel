package com.asv.hotel.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    MANAGER("Менеджер отеля"),
    ADMIN("Администратор отеля"),
    VISITOR("Посетитель отеля"),
    STAFF("Персонал отеля");
    private final String description;
}

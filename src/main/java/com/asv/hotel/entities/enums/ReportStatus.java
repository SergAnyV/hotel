package com.asv.hotel.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportStatus {
    SUBMITTED("подтвержден"),
    APPROVED("одобрен"),
    REJECTED("отклонен"),
    FIXED("исправлен"),
    CANCELLED("отменен"),
    ISSUE("выпущен");

    private final String description;
}

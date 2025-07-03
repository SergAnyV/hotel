package com.asv.hotel.entities.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportStatus {
    SUBMITTED(""),
    APPROVED(""),
    REJECTED(""),
    FIXED(""),
    CANCELLED(""),
    ISSUE("");

    private final String description;
}

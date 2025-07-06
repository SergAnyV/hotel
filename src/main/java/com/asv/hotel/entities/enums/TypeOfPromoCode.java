package com.asv.hotel.entities.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TypeOfPromoCode {
    FIXED("фиксированая скидка "),
    PERCENT("скидка в виде процента от стоимости");

    private final String descriotion;

}

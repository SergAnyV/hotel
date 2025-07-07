package com.asv.hotel.dto;

import com.asv.hotel.entities.enums.TypeOfPromoCode;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
public class PromoCodeDTO {

    private String code;

    private TypeOfPromoCode typeOfPromoCode;

    private BigDecimal discountValue;

    private LocalDate validFromDate;

    private LocalDate validUntilDate;

    private Boolean isActive;

}

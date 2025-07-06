package com.asv.hotel.entities;

import com.asv.hotel.entities.enums.TypeOfPromoCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "discount_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TypeOfPromoCode typeOfPromoCode;

    @Column(name = "discount_value", nullable = false, precision = 4, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFromDate;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntilDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;


}

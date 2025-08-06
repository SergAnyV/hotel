package com.asv.hotel.services;

import com.asv.hotel.entities.PromoCode;

public interface PromoCodeInternalService extends PromoCodeService{

    PromoCode findActivePromoCodeByName(String code);
}

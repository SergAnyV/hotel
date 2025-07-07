package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.PromoCodeDTO;
import com.asv.hotel.entities.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PromoCodeMapper {
    PromoCodeMapper INSTANCE= Mappers.getMapper(PromoCodeMapper.class);

    PromoCodeDTO promoCodeToPromoCodeDTO(PromoCode promoCode);
    PromoCode promoCodeDTOToPromoCode(PromoCodeDTO promoCodeDTO);

}

package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.promocodedto.PromoCodeDTO;
import com.asv.hotel.entities.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PromoCodeMapper {
    PromoCodeMapper INSTANCE= Mappers.getMapper(PromoCodeMapper.class);


    PromoCodeDTO promoCodeToPromoCodeDTO(PromoCode promoCode);

    @Mapping(target = "id",ignore = true)
    PromoCode promoCodeDTOToPromoCode(PromoCodeDTO promoCodeDTO);

}

package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.entities.ServiceHotel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceHoteMapper {
    ServiceHoteMapper INSTANCE= Mappers.getMapper(ServiceHoteMapper.class);

    ServiceHotel ServiceDTOToService(ServiceHotelDTO serviceHotelDTO);

    ServiceHotelDTO ServiceToServiceDTO(ServiceHotel service);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target ="createdAt",ignore = true )
    @Mapping(target ="updatedAt",ignore = true )
    void updateService(ServiceHotelDTO serviceHotelDTO, @MappingTarget ServiceHotel service);
}

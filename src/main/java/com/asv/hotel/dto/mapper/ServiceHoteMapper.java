package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelSimpleDTO;
import com.asv.hotel.entities.ServiceHotel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceHoteMapper {
    ServiceHoteMapper INSTANCE= Mappers.getMapper(ServiceHoteMapper.class);
    @Mapping(target ="title" ,source = "title")
    @Mapping(target ="description" ,source = "description")
    @Mapping(target ="price" ,source = "price")
    ServiceHotel serviceDTOToService(ServiceHotelDTO serviceHotelDTO);

    @Mapping(target ="title" ,source = "title")
    @Mapping(target ="description" ,source = "description")
    @Mapping(target ="price" ,source = "price")
    ServiceHotelDTO serviceToServiceDTO(ServiceHotel service);

    @Mapping(target ="title" ,source = "title")
    @Mapping(target ="description" ,source = "description")
    ServiceHotelDTO serviceHoteSimpleDTOToServiceHotelDTO(ServiceHotelSimpleDTO serviceHotelSimpleDTO);

    @Mapping(target ="title" ,source = "title")
    @Mapping(target ="description" ,source = "description")
    ServiceHotelSimpleDTO serviceHotelDTOToServiceHotelSimpleDTO(ServiceHotelDTO serviceHotelDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target ="createdAt",ignore = true )
    @Mapping(target ="updatedAt",ignore = true )
    void updateService(ServiceHotelDTO serviceHotelDTO, @MappingTarget ServiceHotel service);


}

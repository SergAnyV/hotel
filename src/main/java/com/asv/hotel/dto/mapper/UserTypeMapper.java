package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.UserTypeDTO;
import com.asv.hotel.entities.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserTypeMapper {
    UserTypeMapper INSTANCE= Mappers.getMapper(UserTypeMapper.class);

    UserTypeDTO userTypeToUserTypeDTO(UserType userType);

    UserType UserTypeDTOToUserType(UserTypeDTO userTypeDTO);
}

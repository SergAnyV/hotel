package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.dto.usertypedto.UserTypeSimpleDTO;
import com.asv.hotel.entities.UserType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserTypeMapper {
    UserTypeMapper INSTANCE= Mappers.getMapper(UserTypeMapper.class);

    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source = "description")
    @Mapping(target = "role",source = "role")
    @Mapping(target = "isActive",source = "isActive")
    @Mapping(target = "jobTypeList",source = "jobTypeList")
    UserTypeDTO userTypeToUserTypeDTO(UserType userType);

    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source = "description")
    @Mapping(target = "role",source = "role")
    @Mapping(target = "isActive",source = "isActive")
    @Mapping(target = "jobTypeList",source = "jobTypeList")
    UserType UserTypeDTOToUserType(UserTypeDTO userTypeDTO);

    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source = "description")
    UserTypeSimpleDTO UserTypeToUserTypeSimpleDTO(UserType userType);

    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source = "description")
    UserTypeSimpleDTO UserTypeDTOToUserTypeSimpleDTO(UserTypeDTO userTypeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateuserTypeFromuserTypeDTO(UserTypeDTO userTypeDTO, @MappingTarget UserType userType);
}

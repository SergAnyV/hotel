package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.UserDTO;
import com.asv.hotel.dto.UserSimpleDTO;
import com.asv.hotel.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses= UserTypeMapper.class)
public interface UserMapper {
    UserMapper INSTANCE= Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", source = "role.role")
    UserDTO userToUserDTO(User user);

    @Mapping(target = "role", ignore = true)
    User userDTOToUser(UserDTO userDTO);

    UserSimpleDTO userToUserSimpleDTO (User user);

    UserDTO userSimpleDTOToUseDTO(UserSimpleDTO userSimpleDTO);


}

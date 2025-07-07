package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.UserDTO;
import com.asv.hotel.dto.UserSimpleDTO;
import com.asv.hotel.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UserTypeMapper.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", source = "role.role")
    @Mapping(target = "bookingSet", ignore = true)
    @Mapping(target = "reports", ignore = true)
    UserDTO userToUserDTO(User user);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "bookingSet", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User userDTOToUser(UserDTO userDTO);

    @Mapping(target = "nickName", source = "nickName")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "fathersName", source = "fathersName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    UserSimpleDTO userToUserSimpleDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "bookingSet", ignore = true)
    @Mapping(target = "reports", ignore = true)
    UserDTO userSimpleDTOToUseDTO(UserSimpleDTO userSimpleDTO);


}

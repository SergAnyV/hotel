package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.userdto.UserDTO;
import com.asv.hotel.dto.userdto.UserSimpleDTO;
import com.asv.hotel.entities.User;
import com.asv.hotel.services.UserTypeService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(uses = {UserTypeMapper.class, BookingMapper.class, NotificationMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "bookingSet", ignore = true)
    @Mapping(target = "reports", ignore = true)
    UserDTO userToUserDTO(User user);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "bookingSet", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    User userDTOToUser(UserDTO userDTO);

    @Mapping(target = "nickName", source = "nickName")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "fathersName", source = "fathersName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    UserSimpleDTO userToUserSimpleDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "bookingSet", ignore = true)
    @Mapping(target = "reports", ignore = true)
    UserDTO userSimpleDTOToUseDTO(UserSimpleDTO userSimpleDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "type", ignore = true) // Обрабатывается отдельно в updateUserFromDto
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    void updateUserFieldsFromDto(UserDTO dto, @MappingTarget User user);

    default void updateUserFromDto(UserDTO dto, @MappingTarget User user, UserTypeService userTypeService) {
        updateUserFieldsFromDto(dto, user);
        // роль отдельно обрабатывается
        if (dto.getType() != null) {
            user.setType(userTypeService.findActiveUserTypeByType(dto.getType()));
        }
    }
    @Named("encodePassword")
    default String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            return null;
        }
        return getPasswordEncoder().encode(rawPassword);
    }

   // создание нового PasswordEncoder
    default PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }


}

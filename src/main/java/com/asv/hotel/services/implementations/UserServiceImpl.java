package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.userdto.UserDTO;
import com.asv.hotel.dto.mapper.UserMapper;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.HotelDataAlreadyExistsException;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.repositories.UserRepository;
import com.asv.hotel.services.UserInternalService;
import com.asv.hotel.services.UserTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserInternalService {
    private final UserRepository userRepository;
    private final UserTypeService userTypeService;


    @Transactional
    public UserDTO createUser(UserDTO userDTO) {

        if (!userRepository.findUserByLastNameAndFirstName(userDTO.getLastName(), userDTO.getFirstName()).isEmpty()) {
            log.warn("Error: такой user уже существует {} {}", userDTO.getFirstName(), userDTO.getLastName());
            throw new HotelDataAlreadyExistsException(
                    String.format("такой user уже существует '%s'  '%s'", userDTO.getFirstName(), userDTO.getLastName()));
        }

        UserType userType = userTypeService.findUserTypeByType(userDTO.getType());
        if(userType==null){
            log.warn("Error: данного типа пользователя не существует {}" , userDTO.getType());
            throw new HotelDataNotFoundException(
                    String.format("Такого типа юзера не существует '%s'",userDTO.getType())
            );
        }

        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        user.setType(userType);
        return UserMapper.INSTANCE.userToUserDTO(
                userRepository.save(user));
    }

    @Transactional
    public UserDTO findUserDTOByLastNameAndFirstName(String lastName, String firstName) {
        return UserMapper.INSTANCE.userToUserDTO(
                userRepository.findUserByLastNameAndFirstName(lastName, firstName).orElse(null));
    }

    @Override
    public User findUserByLastNameAndFirstName(String lastName, String firstName) {
        return userRepository.findUserByLastNameAndFirstName(lastName, firstName).orElse(null);
    }

    @Override
    public User findUserByNickName(String nickName) {
        return userRepository.findUserByNickName(nickName).orElse(null);
    }

    @Override
    public UserType findUserTypeByUserNickName(String nickName){
        return userRepository.findUserTypeByUserNickName(nickName).orElse(null);
    }

    @Transactional
    public void deleteUserByLastNameAndFirstName(String lastName, String firstName) {
        if (userRepository.deleteUserByLastNameAndFirstName(lastName, firstName) == 0) {
            log.warn("Error : такого юзера не существует для удаления");
            throw new HotelDataNotFoundException(" такого юзера не существует");
        }
    }

    @Transactional
    public UserDTO findUserDTOByPhoneNumber(String phoneNumber) {
        return UserMapper.INSTANCE.userToUserDTO(userRepository.findUserByPhoneNumber(phoneNumber).orElse(null));
    }

    @Transactional
    public UserDTO changeDataUser(UserDTO userDTO) {
        User existingUser = userRepository.findUserByLastNameAndFirstName(userDTO.getLastName(), userDTO.getFirstName())
                .orElseThrow(() -> new HotelDataNotFoundException("User not found"));
        UserMapper.INSTANCE.updateUserFromDto(userDTO, existingUser, userTypeService);
        userRepository.save(existingUser);
        return UserMapper.INSTANCE.userToUserDTO(existingUser);
    }

}

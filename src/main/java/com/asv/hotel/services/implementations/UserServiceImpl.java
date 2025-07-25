package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.userdto.UserDTO;
import com.asv.hotel.dto.mapper.UserMapper;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.DataAlreadyExistsException;
import com.asv.hotel.exceptions.DataNotFoundException;
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
        try {
            if (!userRepository.findUserByLastNameAndFirstName(userDTO.getLastName(), userDTO.getFirstName()).isEmpty()) {
                log.warn("Error: такой user уже существует {} {}", userDTO.getFirstName(), userDTO.getLastName());
                throw new DataAlreadyExistsException(userDTO.getFirstName() + " " + userDTO.getLastName());
            }
            UserType userType = userTypeService.findUserTypeByType(userDTO.getRole());
            User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
            user.setRole(userType);
            return UserMapper.INSTANCE.userToUserDTO(userRepository.save(user));
        } catch (DataAccessException e) {
            log.error("Error: проблема с доступом к базе данных"
                    , e);
            throw new DataAlreadyExistsException(userDTO.getFirstName() + " " + userDTO.getLastName());
        }
    }

    @Transactional
    public UserDTO findUserDTOByLastNameAndFirstName(String lastName, String firstName) {
        try {
            return UserMapper.INSTANCE.userToUserDTO(userRepository.findUserByLastNameAndFirstName(lastName, firstName).get());
        } catch (Exception ex) {
            log.error("Error : такого юзера не существует {} {} ", lastName, firstName, ex);
            throw new DataNotFoundException(" такого юзера не существует ");
        }
    }


    public User findUserByLastNameAndFirstName(String lastName, String firstName) {
        try {
            return userRepository.findUserByLastNameAndFirstName(lastName, firstName).get();
        } catch (Exception ex) {
            log.warn("Error : такого юзера не существует", ex);
            throw new DataNotFoundException(" такого юзера не существует");
        }
    }

    @Override
    public User findUserByNickName(String nickName) {
        try {
            return   userRepository.findUserByNickName(nickName).get();
        }catch (Exception ex) {
            log.warn("Error : такого юзера не существует {}",nickName, ex);
            throw new DataNotFoundException(" такого юзера не существует");
        }

    }

    @Transactional
    public void deleteUserByLastNameAndFirstName(String lastName, String firstName) {
        if (userRepository.deleteUserByLastNameAndFirstName(lastName, firstName) == 0) {
            log.warn("Error : такого юзера не существует для удаления");
            throw new DataNotFoundException(" такого юзера не существует");
        }
    }

    @Transactional
    public UserDTO findUserDTOByPhoneNumber(String phoneNumber) {
        try {
            return UserMapper.INSTANCE.userToUserDTO(userRepository.findUserByPhoneNumber(phoneNumber).get());
        } catch (Exception ex) {
            log.error("Error : такого юзера не существует", ex);
            throw new DataNotFoundException(" такого юзера не существует");
        }
    }

    @Transactional
    public UserDTO changeDataUser(UserDTO userDTO) {
        User existingUser = userRepository.findUserByLastNameAndFirstName(userDTO.getLastName(), userDTO.getFirstName())
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        UserMapper.INSTANCE.updateUserFromDto(userDTO, existingUser, userTypeService);
        try {
            userRepository.save(existingUser);
        } catch (DataAccessException ex) {
            log.error("Error проблема с обновлением юзера {}", existingUser, ex);
            throw new DataAccessException("Проблема с обновлением юзера ") {
            };
        }

        return UserMapper.INSTANCE.userToUserDTO(existingUser);
    }

}

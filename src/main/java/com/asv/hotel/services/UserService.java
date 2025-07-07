package com.asv.hotel.services;

import com.asv.hotel.dto.UserDTO;
import com.asv.hotel.dto.mapper.UserMapper;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.rooms.DataAlreadyExistsException;
import com.asv.hotel.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserTypeService userTypeService;


    @Transactional
    public UserDTO save(UserDTO userDTO) {
        try {
            if (!userRepository.findUserByLastNameAndFirstName(userDTO.getLastName(), userDTO.getFirstName()).isEmpty()) {
                log.warn("Error: такой user уже существует {} {}", userDTO.getFirstName(), userDTO.getLastName());
                throw new DataAlreadyExistsException(userDTO.getFirstName() + " " + userDTO.getLastName());
            }
            UserType userType = userTypeService.findUserTypeByRoleReturnUserType(userDTO.getRole());
            log.info("{}", userType);
            User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
            user.setRole(userType);
            return UserMapper.INSTANCE.userToUserDTO(userRepository.save(user));
        } catch (DataAccessException e) {
            log.warn("Error: проблема с доступом к базе данных"
                    ,e);
            throw new DataAlreadyExistsException(userDTO.getFirstName() + " " + userDTO.getLastName());
        }
    }

//    @Transactional
//    public UserDTO findUserByLastNameAndFirstName(String lastName, String firstName){
//        try {
//
//            return UserMapper.INSTANCE.userToUserDTO(userRepository.findUserByLastNameAndFirstName(lastName,firstName).get());
//        }catch (DataNotFoundException ex){
//
//        }
//    }


}

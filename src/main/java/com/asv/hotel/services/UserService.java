package com.asv.hotel.services;

import com.asv.hotel.dto.UserDTO;
import com.asv.hotel.dto.UserTypeDTO;
import com.asv.hotel.dto.mapper.UserMapper;
import com.asv.hotel.dto.mapper.UserTypeMapper;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.rooms.DataAlreadyExistsException;
import com.asv.hotel.exceptions.rooms.DataNotFoundException;
import com.asv.hotel.repositories.UserRepository;
import com.asv.hotel.repositories.UserTypeRepository;
import jakarta.persistence.Table;
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
    private final UserTypeRepository userTypeRepository;

    @Transactional
    public UserDTO save(UserDTO userDTO) {
        try {
            if (!userRepository.findUserByLastNameAndFirstName(userDTO.getLastName(), userDTO.getFirstName()).isEmpty()) {
                log.warn("Error: такой user уже существует {} {}", userDTO.getFirstName(), userDTO.getLastName());
                throw new DataAlreadyExistsException(userDTO.getFirstName() + " " + userDTO.getLastName());
            }
//            UserTypeDTO userTypeDTO = userTypeService.findUserTypeByRole(userDTO.getRole());
//            log.info("{}", userTypeDTO);
            UserType userType = userTypeRepository.findUserTypeByRoleLikeIgnoreCase(userDTO.getRole()).orElseThrow(
                    ()->{
                        log.warn("Error: в классе {} проблема в методе {} "
                                ,new Object() {}.getClass().getName()
                                ,new Object() {}.getClass().getEnclosingMethod().getName());
            return new DataNotFoundException("Не существует роли для записи юзеру  ");
        });
            log.info("{}", userType);
            User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
            user.setRole(userType);
            return UserMapper.INSTANCE.userToUserDTO(userRepository.save(user));
        } catch (DataAccessException e) {
            log.warn("Error: проблема с доступом к базе данных , метода {}"
                    , new Object() {
                    }.getClass().getEnclosingMethod().getName());
            throw new DataAlreadyExistsException(userDTO.getFirstName() + " " + userDTO.getLastName());
        }
    }

    @Transactional
    public UserDTO findUserByLastNameAndFirstName(String lastName, String firstName){
        try {

            return UserMapper.INSTANCE.userToUserDTO(userRepository.findUserByLastNameAndFirstName(lastName,firstName).get());
        }catch (DataNotFoundException ex){

        }
    }


}

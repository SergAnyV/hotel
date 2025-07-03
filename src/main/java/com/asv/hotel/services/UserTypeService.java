package com.asv.hotel.services;

import com.asv.hotel.dto.UserTypeDTO;
import com.asv.hotel.dto.mapper.UserTypeMapper;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.rooms.DataAlreadyExistsException;
import com.asv.hotel.exceptions.rooms.DataNotFoundException;
import com.asv.hotel.repositories.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTypeService {
   private final UserTypeRepository userTypeRepository;

    @Transactional
    public UserTypeDTO save(UserTypeDTO userTypeDTO) {
        try {
            if (userTypeRepository.findUserTypeByRoleLikeIgnoreCase(userTypeDTO.getRole()).isPresent()) {
                log.warn("Error: такая роль уже существует {} ", userTypeDTO.getRole());
                throw new DataAlreadyExistsException(userTypeDTO.getRole());
            }
            UserType userType = UserTypeMapper.INSTANCE.UserTypeDTOToUserType(userTypeDTO);
            return UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(userTypeRepository.save(userType));
        } catch (DataAccessException ex){
            log.warn("Error: проблема с доступом к базе данных , метода {}",new Object(){}.getClass().getEnclosingMethod().getName() );
            throw new DataAlreadyExistsException(userTypeDTO.getRole());
        }
    }

    @Transactional
    public void delete(String role){
        if(userTypeRepository.deleteByRole(role)==0){
            log.warn("Error: такая роль не существует {} ", role);
            throw new DataNotFoundException(role);
        }
    }

}

package com.asv.hotel.services;

import com.asv.hotel.dto.UserTypeDTO;
import com.asv.hotel.dto.mapper.UserTypeMapper;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.mistakes.DataAlreadyExistsException;
import com.asv.hotel.exceptions.mistakes.DataNotFoundException;
import com.asv.hotel.repositories.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        } catch (DataAccessException ex) {
            log.warn("Error: проблема с доступом к базе данных ", ex);
            throw new DataAlreadyExistsException(userTypeDTO.getRole());
        }
    }

    @Transactional
    public List<UserTypeDTO> findAll(){
     return userTypeRepository.findAll().stream()
                .map(userType->UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(userType))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(String role) {
        if (userTypeRepository.deleteByRole(role) == 0) {
            log.warn("Error: такая роль не существует {} ", role);
            throw new DataNotFoundException(role);
        }
    }

    @Transactional
    public void deleteAll() {
        try {
            userTypeRepository.deleteAll();
        } catch (RuntimeException ex) {
            log.warn("Error: не удалось очистить список user_types ",ex);
            throw new RuntimeException("не удалось очистить список user_types");
        }
    }

    @Transactional
    public UserTypeDTO findUserTypeByRole(String role){
        try{
         Optional<UserType> userTypeOptional=  userTypeRepository.findUserTypeByRoleLikeIgnoreCase(role);
          if(userTypeOptional.isEmpty()){
              log.warn("Error: роль не распознана среди доступных ,указана {}",role);
              throw new DataNotFoundException("данная роль не распознана в базе");
          }
          return UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(userTypeOptional.get());
        }catch (DataAccessException ex){
            log.warn("Error: проблема с доступом к базе данных ",
                    ex );
            throw new DataAlreadyExistsException(role);
        }
    }

    @Transactional
   public UserType findUserTypeByRoleReturnUserType(String role){
        try{
            Optional<UserType> userTypeOptional=  userTypeRepository.findUserTypeByRoleLikeIgnoreCase(role);
            if(userTypeOptional.isEmpty()){
                log.warn("Error: роль не распознана среди доступных ,указана {}",role);
                throw new DataNotFoundException("данная роль не распознана в базе");
            }
            return userTypeOptional.get();
        }catch (DataAccessException ex){
            log.warn("Error: проблема с доступом к базе данных ",
                    ex );
            throw new DataAlreadyExistsException(role);
        }
    }

}

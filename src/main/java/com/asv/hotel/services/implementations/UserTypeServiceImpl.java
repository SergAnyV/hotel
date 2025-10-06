package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.dto.mapper.UserTypeMapper;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.HotelDataAlreadyExistsException;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.repositories.UserTypeRepository;
import com.asv.hotel.services.UserTypeInternalService;
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
public class UserTypeServiceImpl implements UserTypeInternalService {

    private final UserTypeRepository userTypeRepository;

    @Transactional
    public UserTypeDTO createUserType(UserTypeDTO userTypeDTO) {
        if (userTypeRepository.findUserTypeByRoleLikeIgnoreCase(userTypeDTO.getName()).isPresent()) {
            log.warn("Error: такая роль уже существует {} ", userTypeDTO.getName());
            throw new HotelDataAlreadyExistsException(userTypeDTO.getName());
        }
        UserType userType = UserTypeMapper.INSTANCE.UserTypeDTOToUserType(userTypeDTO);
        return UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(userTypeRepository.save(userType));
    }

    @Transactional
    public List<UserTypeDTO> findAllUserTypeDTOs() {
        return userTypeRepository.findAll().stream()
                .map(userType -> UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(userType))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserTypeByType(String role) {
        if (userTypeRepository.deleteByRole(role) == 0) {
            log.warn("Error: такая роль не существует {} ", role);
            throw new HotelDataNotFoundException(role);
        }
    }

    @Transactional
    public void deleteAllUserTypes() {
        userTypeRepository.deleteAll();
    }

    @Transactional
    public UserTypeDTO findUserTypeDTOByType(String role) {
        Optional<UserType> userTypeOptional = userTypeRepository.findUserTypeByRoleLikeIgnoreCase(role);
        return UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(userTypeOptional.get());
    }

    @Transactional
    public UserType findUserTypeByType(String role) {
        Optional<UserType> userTypeOptional = userTypeRepository.findUserTypeByRoleLikeIgnoreCase(role);
        return userTypeOptional.get();
    }

    @Transactional
    public UserTypeDTO cahngeDataUserType(UserTypeDTO userTypeDTO) {
        Optional<UserType> userTypeOptional = userTypeRepository.findUserTypeByRoleLikeIgnoreCase(userTypeDTO.getName());
        if (userTypeOptional.isEmpty()) {
            log.warn("Error: роль не распознана среди доступных ,указана {}", userTypeDTO.getName());
            throw new HotelDataNotFoundException(String.format("данная роль не распознана в базе '%s'",
                    userTypeDTO.getName()));
        }
        UserType userType = userTypeOptional.get();
        UserTypeMapper.INSTANCE.updateuserTypeFromuserTypeDTO(userTypeDTO, userType);
        userTypeRepository.save(userType);
        return UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(userType);
    }


    public UserType findActiveUserTypeByType(String role) {
        Optional<UserType> userTypeOptional = userTypeRepository.findUserTypeByRoleLikeIgnoreCase(role);
        if (userTypeOptional.isEmpty() || !userTypeOptional.get().getIsActive()) {
            log.warn("Error: роль не распознана среди доступных(активных) ,указана {}", role);
            throw new HotelDataNotFoundException(String.format("данная роль не распознана в базе '%s'",
                    role));
        }
        return userTypeOptional.get();
    }

    @Override
    public List<UserType> findUserTypesByJobTypeId(Long jobTypeId) {
        return userTypeRepository.findUserTypesByJobTypeId(jobTypeId);
    }
}

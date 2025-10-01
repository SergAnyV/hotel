package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.jobtypedto.JobTypeDTO;
import com.asv.hotel.dto.jobtypedto.JobTypeSimpleDTO;
import com.asv.hotel.dto.mapper.JobTypeMapper;
import com.asv.hotel.dto.mapper.UserTypeMapper;
import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.entities.JobType;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.HotelDataAlreadyExistsException;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.repositories.JobTypeRepository;
import com.asv.hotel.services.JobTypeInternalService;

import com.asv.hotel.services.UserTypeInternalService;
import com.asv.hotel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobTypeServiceImpl implements JobTypeInternalService {
    private final JobTypeRepository jobTypeRepository;
    private final UserTypeInternalService userTypeInternalService;

    @Transactional
    @Override
    public List<JobTypeDTO> findAll() {

        List<JobType> jobTypeList = jobTypeRepository.findAll();
        jobTypeList.forEach(jobType -> Hibernate.initialize(jobType.getUserTypes()));

        return jobTypeList.stream()
                .map(jp -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jp))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public JobTypeDTO createJobType(JobTypeSimpleDTO jobTypeSimpleDTO) {
        JobType jobType = JobTypeMapper.INSTANCE.jobTypeSimpleDTOToJobType(jobTypeSimpleDTO);
        jobType.setIsActive(true);
        try {
            jobType = jobTypeRepository.save(jobType);
            JobTypeDTO result = JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType);
            return result;
        } catch (DataIntegrityViolationException ex) {
            log.error("Error: не пройдена проверка на уникальность полей jobType при сохранении в методе createJobType"
                    + "jobTypeSimpleDTO ={}", jobTypeSimpleDTO, ex);
            throw new HotelDataAlreadyExistsException(" Fileds is not unique for this jobTypeSimpleDTO=" + jobTypeSimpleDTO);
        }
    }

    @Transactional
    @Override
    public List<JobTypeDTO> findJobTypesDTOByTitle(String title) {
        title = cleanString(title);
        title = StringUtil.preparedStringForPartiallyCoincidence(title);
        try {
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitle(title);
            return listJT.stream()
                    .map(jobType -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType))
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            log.error("Error: проблемы с поиском  jobType по title= {}  в методе  findJobTypesDTOByTitle", title, ex);
            throw new HotelDataNotFoundException("There is no this title= " + title);
        }
    }

    @Transactional
    @Override
    public List<JobType> findJobTypesByTitle(String title) {
        title = cleanString(title);
        title = StringUtil.preparedStringForPartiallyCoincidence(title);
        try {
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitle(title);
            return listJT;
        } catch (DataAccessException ex) {
            log.error("Error проблемы с поиском  jobType по title= {} в методе findJobTypesDTOByTitle", title, ex);
            throw new HotelDataNotFoundException("There is no this title " + title);
        }
    }

    @Transactional
    @Override
    public List<JobTypeDTO> findActiveJobTypesDTOByTitle(String title) {
        title = cleanString(title);
        title = StringUtil.preparedStringForPartiallyCoincidence(title);
        try {
            List<JobType> listJT = jobTypeRepository.findActiveJobTypesByTitle(title);
            return listJT.stream()
                    .map(jobType -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType))
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            log.error("Error проблемы с поиском  jobType по title= {} в методе findActiveJobTypesDTOByTitle", title, ex);
            throw new HotelDataNotFoundException("There is no this title " + title);
        }
    }

    @Transactional
    @Override
    public List<JobType> findActiveJobTypesByTitle(String title) {
        title = cleanString(title);
        title = StringUtil.preparedStringForPartiallyCoincidence(title);
        try {
            List<JobType> listJT = jobTypeRepository.findActiveJobTypesByTitle(title);
            return listJT;
        } catch (DataAccessException ex) {
            log.error("Error проблемы с поиском  jobType по title= {}", title, ex);
            throw new HotelDataNotFoundException("There is no this title " + title);
        }
    }

    @Transactional
    @Override
    public List<JobTypeDTO> findJobTypesDTOByTitleAndActiveStatusWithoutUserType(String title, String isActive) {
        isActive = cleanString(isActive);
        title = cleanString(title);
        Boolean isActiveBoolean = Boolean.valueOf(isActive);
        try {
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitleAndActiveStatusWithoutUserType(title, isActiveBoolean);
            return listJT.stream()
                    .map(jt -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jt))
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            log.error("Error Непредвиденные проблемы с поиском  jobType по title= {} или статусу ={} в методе " +
                    "findJobTypesDTOByTitleAndActiveStatusWithoutUserType", title, isActive,ex);
            throw new HotelDataNotFoundException("Incorrect title or active status" + title + " " + isActive);
        }
    }

    @Transactional
    @Override
    public List<JobType> findJobTypesByTitleAndActiveStatusWithoutUserType(String title, String isActive) {
        Boolean isActiveBoolean = Boolean.valueOf(isActive);
        try {
            title = cleanString(title);
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitleAndActiveStatusWithoutUserType(title, isActiveBoolean);
            return listJT;
        } catch (DataAccessException ex) {
            log.error("Error: проблемы с поиском  jobType по title {} и статусу {} в методе " +
                    "findJobTypesByTitleAndActiveStatusWithoutUserType", title, isActive,ex);
            throw new HotelDataNotFoundException("Incorrect title or active status" + title + " " + isActive);
        }
    }

    @Transactional
    @Override
    public void deleteJobTypeById(Long id) {
        if (jobTypeRepository.deleteJobTypeById(id) == 0) {
            log.error("Error: не существует jobtype с id= {} для удаления с помощью deleteJobTypeById", id);
            throw new HotelDataNotFoundException("There is no JobType for delete with this id =" + id);
        }

    }

    @Transactional
    public void deleteJobTypeByTitle(String title) {
        if (jobTypeRepository.deleteJobTypeByTitle(title) == 0) {
            log.error("Error: не существует jobtype с title= {}  для удаления с помощью deleteJobTypeByTitle", title);
            throw new HotelDataNotFoundException("There is no JobType for delete with this title =" + title);
        }
    }

    @Transactional
    public void updateJobTypesDescriptionAndActiveStatusByTitle(String title, String description, String status) {
        Boolean statusB = Boolean.valueOf(status);
        if (jobTypeRepository.updateJobTypesDescriptionAndActiveStatusByTitle(title, description, statusB) == 0) {
            throw new HotelDataNotFoundException("Problem with updating JobType");
        }
    }

    @Transactional
    public Set<UserTypeDTO> addUserTypeToJobType(String jobTypeTitle, String userTypeRole) {

            jobTypeTitle = cleanString(jobTypeTitle);
            userTypeRole = cleanString(userTypeRole);
        try {
            JobType jobType = jobTypeRepository.findJobTypesByTitle(jobTypeTitle).get(0);
            Set<UserType> userTypeSet = jobType.getUserTypes();
            UserType userType = userTypeInternalService.findActiveUserTypeByType(userTypeRole);
            userTypeSet.add(userType);
            jobType = jobTypeRepository.save(jobType);
            return jobType.getUserTypes().stream().map(ut -> UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(ut))
                    .collect(Collectors.toSet());
        } catch (DataAccessException ex) {
            log.error("Problem with adding UserType to JobType jobType is {} userType is {}", jobTypeTitle, userTypeRole,ex);
            throw new HotelDataNotFoundException("Problem with adding UserType to JobType jobTyp");
        }

    }


    private String cleanString(String line) {
        return line.trim().toLowerCase();
    }


}

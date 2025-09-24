package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.jobtypedto.JobTypeDTO;
import com.asv.hotel.dto.jobtypedto.JobTypeSimpleDTO;
import com.asv.hotel.dto.mapper.JobTypeMapper;
import com.asv.hotel.dto.mapper.UserTypeMapper;
import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.entities.JobType;
import com.asv.hotel.entities.UserType;
import com.asv.hotel.exceptions.DataAlreadyExistsException;
import com.asv.hotel.exceptions.DataNotFoundException;
import com.asv.hotel.repositories.JobTypeRepository;
import com.asv.hotel.services.JobTypeInternalService;

import com.asv.hotel.services.UserTypeInternalService;
import com.asv.hotel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            return jobTypeRepository.findAll().stream()
                    .map(jp -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jp))
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            log.error("Error проблемы с получением всего списка работ", ex);
            throw ex;
        }
    }

    @Transactional
    @Override
    public JobTypeDTO createJobType(JobTypeSimpleDTO jobTypeSimpleDTO) {
        try {
            JobType jobType = JobTypeMapper.INSTANCE.jobTypeSimpleDTOToJobType(jobTypeSimpleDTO);

            jobType.setIsActive(true);
            JobTypeDTO result = JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType);
            return result;
        } catch (RuntimeException ex) {
            log.error("Error проблемы с сохранением типа работ ");
            throw new DataAlreadyExistsException("This job type has been created before");
        }
    }

    @Transactional
    @Override
    public List<JobTypeDTO> findJobTypesDTOByTitle(String title) {
        try {

            title = cleanString(title);
            title = StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitle(title);

            return listJT.stream()
                    .map(jobType -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType))
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw new DataNotFoundException("There is no this title " + title);
        }
    }

    @Transactional
    @Override
    public List<JobType> findJobTypesByTitle(String title) {
        try {

            title = cleanString(title);
            title = StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitle(title);
            return listJT;
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw new DataNotFoundException("There is no this title " + title);
        }
    }

    @Transactional
    @Override
    public List<JobTypeDTO> findActiveJobTypesDTOByTitle(String title) {
        try {
            title = cleanString(title);
            title = StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findActiveJobTypesByTitle(title);

            return listJT.stream()
                    .map(jobType -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType))
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw new DataNotFoundException("There is no this title " + title);
        }
    }

    @Transactional
    @Override
    public List<JobType> findActiveJobTypesByTitle(String title) {
        try {

            title = cleanString(title);
            title = StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findActiveJobTypesByTitle(title);

            return listJT;
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw new DataNotFoundException("There is no this title " + title);
        }
    }

    @Transactional
    @Override
    public List<JobTypeDTO> findJobTypesDTOByTitleAndActiveStatusWithoutUserType(String title, String isActive) {
        try {
            Boolean isActiveBoolean = StringUtil.convertStringToBoolean(isActive);
            title = cleanString(title);

            if (isActiveBoolean.equals(null)) {
                log.error("Error проблемы с поиском  jobType по статусу {}в методе " +
                        "findJobTypesDTOByTitleAndActiveStatusWithoutUserType", isActiveBoolean);
                throw new DataNotFoundException("There is no this status " + isActive);
            }
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitleAndActiveStatusWithoutUserType(title, isActiveBoolean);

            return listJT.stream()
                    .map(jt -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jt))
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            log.error("Error Непредвиденные проблемы с поиском  jobType по title {} или статусу {}в методе " +
                    "findJobTypesDTOByTitleAndActiveStatusWithoutUserType", title, isActive);
            throw new DataNotFoundException("Incorrect data");
        }
    }

    @Transactional
    @Override
    public List<JobType> findJobTypesByTitleAndActiveStatusWithoutUserType(String title, String isActive) {
        try {
            Boolean isActiveBoolean = StringUtil.convertStringToBoolean(isActive);
            title = cleanString(title);

            List<JobType> listJT = jobTypeRepository.findJobTypesByTitleAndActiveStatusWithoutUserType(title, isActiveBoolean);

            return listJT;

        } catch (RuntimeException ex) {
            log.error("Error Непредвиденные проблемы с поиском  jobType по title {} или статусу {}в методе " +
                    "findJobTypesByTitleAndActiveStatusWithoutUserType", title, isActive);
            throw new DataNotFoundException("Incorrect data");
        }
    }

    @Transactional
    @Override
    public void deleteJobTypeById(Long id) {

        if (jobTypeRepository.deleteJobTypeById(id) == 0) {
            throw new DataNotFoundException("There is no JobType with this id");
        }

    }

    @Transactional
    public void deleteJobTypeByTitle(String tittle) {

        if (jobTypeRepository.deleteJobTypeByTitle(tittle) == 0) {
            throw new DataNotFoundException("There is no JobType with this title");
        }
    }

    @Transactional
    public void updateJobTypesDescriptionAndActiveStatusByTitle(String title, String description, String status) {
        Boolean statusB = StringUtil.convertStringToBoolean(status);
        if (jobTypeRepository.updateJobTypesDescriptionAndActiveStatusByTitle(title, description, statusB) == 0) {
            throw new DataNotFoundException("Problem with updating JobType");
        }
    }

    @Transactional
    public Set<UserTypeDTO> addUserTypeToJobType(String jobTypeTitle, String userTypeRole) {
        try {
            jobTypeTitle = cleanString(jobTypeTitle);
            userTypeRole = cleanString(userTypeRole);
            JobType jobType = jobTypeRepository.findJobTypesByTitle(jobTypeTitle).get(0);
            Set<UserType> userTypeSet = jobType.getUserTypes();
            UserType userType = userTypeInternalService.findActiveUserTypeByType(userTypeRole);
            userTypeSet.add(userType);
            jobType =jobTypeRepository.save(jobType);
            return jobType.getUserTypes().stream().map(ut-> UserTypeMapper.INSTANCE.userTypeToUserTypeDTO(ut))
                    .collect(Collectors.toSet());
        }catch (RuntimeException ex){
            log.error("Problem with adding UserType to JobType jobType is {} userType is {}",jobTypeTitle,userTypeRole);
            throw new DataNotFoundException("Problem with adding UserType to JobType jobTyp");
        }

    }


    private String cleanString(String line) {
        return line.trim().toLowerCase();
    }


}

package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.jobtypedto.JobTypeDTO;
import com.asv.hotel.dto.jobtypedto.JobTypeSimpleDTO;
import com.asv.hotel.dto.mapper.JobTypeMapper;
import com.asv.hotel.entities.JobType;
import com.asv.hotel.exceptions.DataAlreadyExistsException;
import com.asv.hotel.exceptions.DataNotFoundException;
import com.asv.hotel.repositories.JobTypeRepository;
import com.asv.hotel.services.JobTypeService;
import com.asv.hotel.services.UserTypeInternalServiice;
import com.asv.hotel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobTypeServiceImpl implements JobTypeService {
    private final JobTypeRepository jobTypeRepository;
    private final UserTypeInternalServiice userTypeInternalServiice;

    @Transactional
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
    public List<JobTypeDTO> findJobTypesDTOByTitle(String title) {
        try {
            if (title.isBlank() || title.equals(null)) {
                log.error("Error проблемы с поиском  jobType по title {} в методе findJobTypesDTOByTitle", title);
                throw new DataNotFoundException("Incorrect Title");
            }
            title = cleanString(title);
            title= StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitle(title);

            return listJT.stream()
                    .map(jobType -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType))
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw ex;
        }
    }

    @Transactional
    public List<JobType> findJobTypesByTitle(String title) {
        try {
            if (title.isBlank() || title.equals(null)) {
                log.error("Error проблемы с поиском  jobType по title {} в методе findJobTypesByTitle", title);
                return null;
            }
            title = cleanString(title);
            title=StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findJobTypesByTitle(title);
            return listJT;
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw ex;
        }
    }

    @Transactional
    public List<JobTypeDTO> findActiveJobTypesDTOByTitle(String title) {
        try {
            if (title.isBlank() || title.equals(null)) {
                log.error("Error проблемы с поиском  jobType по title {} в методе findActiveJobTypesDTOByTitle", title);
                return null;
            }
            title = cleanString(title);
            title=StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findActiveJobTypesByTitle(title);

            return listJT.stream()
                    .map(jobType -> JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jobType))
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw ex;
        }
    }

    @Transactional
    public List<JobType> findActiveJobTypesByTitle(String title) {
        try {

            if (title.isBlank() || title.equals(null)) {
                log.error("Error проблемы с поиском  jobType по title {} в методе findActiveJobTypesByTitle", title);
                return null;
            }

            title = cleanString(title);
            title=StringUtil.preparedStringForPartiallyCoincidence(title);
            List<JobType> listJT = jobTypeRepository.findActiveJobTypesByTitle(title);

            return listJT;
        } catch (RuntimeException ex) {
            log.error("Error проблемы с поиском  jobType по title {}", title, ex);
            throw ex;
        }
    }

    @Transactional
    public List<JobTypeDTO> findJobTypesDTOByTitleAndActiveStatusWithoutUserType(String title, String isActive) {
        try {
            if (title.isBlank() || title.equals(null) || isActive.isBlank() || isActive.equals(null)) {
                log.error("Error проблемы с поиском  jobType по title {} или статусу {}в методе " +
                        "findJobTypesDTOByTitleAndActiveStatusWithoutUserType", title, isActive);
                return null;
            }

            Boolean isActiveBoolean = StringUtil.convertStringToBoolean(isActive);
            title = cleanString(title);

            if (isActiveBoolean.equals(null)) {
                log.error("Error проблемы с поиском  jobType по статусу {}в методе " +
                        "findJobTypesDTOByTitleAndActiveStatusWithoutUserType", isActiveBoolean);
                return null;
            }
            List<JobType> listJT=jobTypeRepository.findJobTypesByTitleAndActiveStatusWithoutUserType(title,isActiveBoolean);

            return listJT.stream()
                    .map(jt->JobTypeMapper.INSTANCE.jobTypeToJobTypeDTO(jt))
                    .collect(Collectors.toList());

        }catch (RuntimeException ex){
            log.error("Error Непредвиденные проблемы с поиском  jobType по title {} или статусу {}в методе " +
                    "findJobTypesDTOByTitleAndActiveStatusWithoutUserType", title, isActive);
            throw new DataNotFoundException("Incorrect data");
        }
    }

    @Transactional
    public List<JobType> findJobTypesByTitleAndActiveStatusWithoutUserType(String title, String isActive) {
        try {
            if (title.isBlank() || title.equals(null) || isActive.isBlank() || isActive.equals(null)) {
                log.error("Error проблемы с поиском  jobType по title {} или статусу {}в методе " +
                        "findJobTypesByTitleAndActiveStatusWithoutUserType", title, isActive);
                return null;
            }

            Boolean isActiveBoolean = StringUtil.convertStringToBoolean(isActive);
            title = cleanString(title);

            if (isActiveBoolean.equals(null)) {
                log.error("Error проблемы с поиском  jobType по статусу {}в методе " +
                        "findJobTypesByTitleAndActiveStatusWithoutUserType", isActiveBoolean);
                return null;
            }
            List<JobType> listJT=jobTypeRepository.findJobTypesByTitleAndActiveStatusWithoutUserType(title,isActiveBoolean);

            return listJT;

        }catch (RuntimeException ex){
            log.error("Error Непредвиденные проблемы с поиском  jobType по title {} или статусу {}в методе " +
                    "findJobTypesByTitleAndActiveStatusWithoutUserType", title, isActive);
            throw new DataNotFoundException("Incorrect data");
        }
    }

    private String cleanString(String line) {
        return line.trim().toLowerCase();
    }





}

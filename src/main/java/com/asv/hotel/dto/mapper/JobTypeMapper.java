package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.jobtypedto.JobTypeDTO;
import com.asv.hotel.dto.jobtypedto.JobTypeSimpleDTO;
import com.asv.hotel.entities.JobType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UserTypeMapper.class)
public interface JobTypeMapper {
    JobTypeMapper INSTANCE = Mappers.getMapper(JobTypeMapper.class);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "userTypes", source = "userTypes")
    JobTypeDTO jobTypeToJobTypeDTO(JobType jobType);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "userTypes", source = "userTypes")
    JobType jobTypeDTOToJobtype(JobTypeDTO jobTypeDTO);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    JobTypeSimpleDTO jobTypeDTOToJobTypeSimpleDTO(JobTypeDTO jobTypeDTO);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    JobTypeDTO jobTypeSimpleDTOToJobTypeDTO(JobTypeSimpleDTO jobTypeDTO);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    JobTypeSimpleDTO jobTypeToJobTypeSimpleDTO(JobType jobType);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    JobType jobTypeSimpleDTOToJobType(JobTypeSimpleDTO jobTypeSimpleDTO);
}

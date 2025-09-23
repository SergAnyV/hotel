package com.asv.hotel.services.implementations;

import com.asv.hotel.repositories.JobTypeRepository;
import com.asv.hotel.services.JobTypeService;
import com.asv.hotel.services.UserTypeInternalServiice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JobTypeServiceImpl implements JobTypeService {
    private final JobTypeRepository jobTypeRepository;
    private final UserTypeInternalServiice userTypeInternalServiice;




}

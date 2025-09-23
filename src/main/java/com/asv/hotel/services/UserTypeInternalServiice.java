package com.asv.hotel.services;

import com.asv.hotel.entities.UserType;

import java.util.List;

public interface UserTypeInternalServiice extends UserTypeService{
    List<UserType> findUserTypesByJobTypeId(Long JobTypeId);
}

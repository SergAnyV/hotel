package com.asv.hotel.repositories;

import com.asv.hotel.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType,Long> {
}

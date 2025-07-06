package com.asv.hotel.repositories;

import com.asv.hotel.entities.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType,Long> {
}

package com.asv.hotel.repositories;

import com.asv.hotel.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query(value ="SELECT * FROM reports WHERE id =:id",nativeQuery = true )
    Optional<Report> findReportById(@Param("id") Long id);

}

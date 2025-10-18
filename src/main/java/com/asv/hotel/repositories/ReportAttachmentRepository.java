package com.asv.hotel.repositories;

import com.asv.hotel.entities.ReportAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportAttachmentRepository extends JpaRepository<ReportAttachment,Long> {

    @Override
    Optional<ReportAttachment> findById(Long id);

}

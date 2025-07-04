package com.asv.hotel.repositories;

import com.asv.hotel.entities.ReportAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportAttachmentRepository extends JpaRepository<ReportAttachment,Long> {
}

package com.asv.hotel.services;

import com.asv.hotel.dto.reportdto.ReportDTO;
import com.asv.hotel.entities.enums.ReportType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportService {

    ReportDTO createReport(ReportType reportType, String roomNumber, List<MultipartFile> multipartFileList);

    void addReportAttachmentToReport(Long reportId, List<MultipartFile> multipartFileList);
    void deleteAttachmentFromReport(Long reportId,Long reportAttachmentId);
}

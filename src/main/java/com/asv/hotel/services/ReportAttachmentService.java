package com.asv.hotel.services;

import com.asv.hotel.dto.reportattachmendto.ReportAttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportAttachmentService {

    ReportAttachmentDTO createReportAttachment(MultipartFile multipartFile);
    List<ReportAttachmentDTO> createReportAttachmentBatch(List<MultipartFile> multipartFileList);
}

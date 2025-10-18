package com.asv.hotel.services;

import com.asv.hotel.dto.reportattachmendto.ReportAttachmentDTO;
import com.asv.hotel.dto.reportattachmendto.ReportAttachmentSimpleDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportAttachmentService {

    ReportAttachmentDTO createReportAttachment(MultipartFile multipartFile);


    List<ReportAttachmentDTO> createReportAttachmentBatch(List<MultipartFile> multipartFileList);


    ByteArrayResource findByteArrayResourceByReportAttachmenID(Long id);


    byte[] findByteArrayByReportAttachmenID(Long id);

    ReportAttachmentSimpleDTO findReportAttachmentSimpleDTOByID(Long id);

}

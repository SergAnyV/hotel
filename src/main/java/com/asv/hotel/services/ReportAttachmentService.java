package com.asv.hotel.services;

import com.asv.hotel.dto.reportattachmendto.ReportAttachmentDTO;
import com.asv.hotel.dto.reportattachmendto.ReportAttachmentSimpleDTO;
import com.asv.hotel.entities.ReportAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportAttachmentService {

//    ReportAttachmentDTO createReportAttachment(MultipartFile multipartFile);
//
//
//    List<ReportAttachmentDTO> createReportAttachmentBatchReturnReportAttachmentDTO(List<MultipartFile> multipartFileList);


    ReportAttachmentSimpleDTO findReportAttachmentSimpleDTOByID(Long id);




}

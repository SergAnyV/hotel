package com.asv.hotel.services;

import com.asv.hotel.entities.ReportAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ReportAttachmentInternalService extends ReportAttachmentService{

    List<ReportAttachment> findReportAttachmentByReportID(Long id);

    ReportAttachment generateReportAttachmentFromMultipartFile(MultipartFile multipartFile);

    Set<ReportAttachment> generateReportAttachmentSetFromMultipartFileList(List<MultipartFile> multipartFileList);

    List<ReportAttachment> findReportAttachmentForZipByReportID(Long id);
}

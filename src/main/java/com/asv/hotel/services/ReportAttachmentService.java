package com.asv.hotel.services;

import com.asv.hotel.dto.reportattachmendto.ReportAttachmentSimpleDTO;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.security.Principal;

public interface ReportAttachmentService {

    ReportAttachmentSimpleDTO findReportAttachmentSimpleDTOByID(Long id);

    void deleteReportAttachmentById(Long id);

    StreamingResponseBody findStreamingResponseBodyAttacmnetsByReportID(Long id);

}

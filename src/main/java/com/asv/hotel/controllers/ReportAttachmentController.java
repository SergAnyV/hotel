package com.asv.hotel.controllers;

import com.asv.hotel.dto.reportattachmendto.ReportAttachmentDTO;
import com.asv.hotel.services.ReportAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class ReportAttachmentController {
    private final ReportAttachmentService reportAttachmentService;

    @PostMapping
    public ResponseEntity<ReportAttachmentDTO> createAttachment(@RequestParam("multipartFile") MultipartFile multipartFile) {
        ReportAttachmentDTO reportAttachmentDTO = reportAttachmentService.createReportAttachment(multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportAttachmentDTO);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ReportAttachmentDTO>> createAttachmentBatch(@RequestParam("multipartFileList") List<MultipartFile> multipartFileList) {
        List<ReportAttachmentDTO> reportAttachmentDTOList = reportAttachmentService.createReportAttachmentBatch(multipartFileList);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportAttachmentDTOList);
    }
}

package com.asv.hotel.controllers;

import com.asv.hotel.dto.reportattachmendto.ReportAttachmentDTO;
import com.asv.hotel.dto.reportattachmendto.ReportAttachmentSimpleDTO;
import com.asv.hotel.services.ReportAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<ReportAttachmentDTO>> createAttachmentBatch(
            @RequestParam("multipartFileList")
            List<MultipartFile> multipartFileList
    ) {
        List<ReportAttachmentDTO> reportAttachmentDTOList = reportAttachmentService.createReportAttachmentBatch(multipartFileList);
        if (reportAttachmentDTOList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reportAttachmentDTOList);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Resource> getContentByAttachmentID(@PathVariable(value = "id") Long id) {
        ReportAttachmentSimpleDTO resource = reportAttachmentService.findReportAttachmentSimpleDTOByID(id);
        if (resource == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("inline; filename=\"%s\"", resource.getFileName()))
                .body(resource.getByteArrayResource());
    }
}

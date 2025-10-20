package com.asv.hotel.controllers;

import com.asv.hotel.dto.reportdto.ReportDTO;
import com.asv.hotel.entities.enums.ReportType;
import com.asv.hotel.services.ReportService;
import com.asv.hotel.services.implementations.ReportServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReportDTO> createReport(@RequestParam("reportType")
                                                  ReportType reportType,
                                                  @RequestParam("roomNumber")
                                                  String roomNumber,
                                                  @RequestParam(value = "multipartFileList", required = false)
                                                  List<MultipartFile> multipartFileList) {
        ReportDTO reportDTO = reportService.createReport(reportType, roomNumber, multipartFileList);
        if (reportDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reportDTO);
    }

    @PostMapping("/{reportId}/attachments")
    public ResponseEntity<?> addReportAttachmentToTheReportByRID(@PathVariable("reportId")
                                                                 @NotNull
                                                                 Long reportId,
                                                                 @RequestParam(value = "multipartFileList", required = false)
                                                                 @NotNull
                                                                 List<MultipartFile> multipartFileList) {
        reportService.addReportAttachmentToReport(reportId, multipartFileList);
        return ResponseEntity.ok().build();
    }
}

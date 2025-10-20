package com.asv.hotel.controllers;

import com.asv.hotel.dto.reportdto.ReportDTO;
import com.asv.hotel.entities.enums.ReportType;
import com.asv.hotel.services.ReportService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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
    public ResponseEntity<Void> addReportAttachmentToTheReportByRID(@PathVariable("reportId")
                                                                    @NotNull
                                                                    @NumberFormat
                                                                    @Positive
                                                                    Long reportId,
                                                                    @RequestParam(value = "multipartFileList", required = false)
                                                                    @NotNull
                                                                    List<MultipartFile> multipartFileList) {
        reportService.addReportAttachmentToReport(reportId, multipartFileList);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reportId}/{reportAttachmentId}")
    public ResponseEntity<Void> deleteAttachFromReportById(@PathVariable("reportId")
                                                           @NotNull
                                                           @NumberFormat
                                                           @Positive
                                                           Long reportId,
                                                           @PathVariable("reportAttachmentId")
                                                           @NotNull
                                                           @NumberFormat
                                                           @Positive
                                                           Long reportAttachmentId) {

        reportService.deleteAttachmentFromReport(reportId, reportAttachmentId);
        return ResponseEntity.noContent().build();
    }
}

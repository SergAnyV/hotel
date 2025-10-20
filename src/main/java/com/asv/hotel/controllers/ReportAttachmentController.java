package com.asv.hotel.controllers;

import com.asv.hotel.dto.reportattachmendto.ReportAttachmentSimpleDTO;
import com.asv.hotel.services.ReportAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.security.Principal;

@Tag(name = "ReportAttachment Management", description = "REST API для управлениями вложенным файлам к отчету")
@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class ReportAttachmentController {
    private final ReportAttachmentService reportAttachmentService;

    @Operation(summary = "Найти вложение его id",
            description = "Возвращает содержимое вложения (изображение )в виде бинарных данных. " +
            "Тип содержимого определяется MIME-типом файла. " +
            "Поддерживается отображение в браузере (inline) для изображений.")
    @ApiResponse(responseCode = "200", description = "Вложение успешно найдено и возвращено")
    @ApiResponse(responseCode = "404", description = "вложение не найдены")
    @GetMapping("/id/{id}")
    public ResponseEntity<Resource> getContentByAttachmentID(@PathVariable(value = "id")
                                                                 @NotNull
                                                                 Long id) {
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

    @GetMapping(value = "/{reportId}/attachments/zip-stream", produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> downloadAttachmentsAsZip(@PathVariable(value = "reportId")
                                                                              @NotNull
                                                                              Long reportId){
        StreamingResponseBody stream=reportAttachmentService.findStreamingResponseBodyAttacmnetsByReportID(reportId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report_" + reportId + "_attachments.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }


    @Operation(summary = "Удалить вложение",
            description = "удаляет данные существующего вложения по id")
    @ApiResponse(responseCode = "204", description = "Вложения удалено")
    @ApiResponse(responseCode = "404", description = "Вложения не найдено")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReportAttachment(@PathVariable(value = "id")
                                                           @NotNull
                                                           Long id){
        reportAttachmentService.deleteReportAttachmentById(id);
        return ResponseEntity.noContent().build();
    }




}

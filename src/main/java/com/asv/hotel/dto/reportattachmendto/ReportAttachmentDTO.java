package com.asv.hotel.dto.reportattachmendto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportAttachmentDTO {


    private Long id;


    private LocalDate createdAt;


    private String fileName;


    private String contentType;


    private Long size;


}

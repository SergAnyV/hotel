package com.asv.hotel.dto.reportattachmendto;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportAttachmentSimpleDTO {

    private String fileName;


    private String contentType;


    private ByteArrayResource byteArrayResource;


}

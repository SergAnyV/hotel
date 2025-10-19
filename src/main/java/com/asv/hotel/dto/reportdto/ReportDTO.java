package com.asv.hotel.dto.reportdto;


import com.asv.hotel.entities.enums.ReportStatus;
import com.asv.hotel.entities.enums.ReportType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;

    private ReportStatus reportStatus;

    private String descriptionStatus;

    private ReportType reportType;

    private String descriptionType;

    private LocalDate createdAt;

    private LocalDate updatedDate;

    private String roomNumber;

    private String ownerNickName;

}

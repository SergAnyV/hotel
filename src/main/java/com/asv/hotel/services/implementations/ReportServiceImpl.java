package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.mapper.ReportMapper;
import com.asv.hotel.dto.reportdto.ReportDTO;
import com.asv.hotel.entities.Report;
import com.asv.hotel.entities.ReportAttachment;
import com.asv.hotel.entities.Room;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.enums.ReportStatus;
import com.asv.hotel.entities.enums.ReportType;
import com.asv.hotel.repositories.ReportRepository;
import com.asv.hotel.services.ReportAttachmentInternalService;
import com.asv.hotel.services.RoomInternalService;
import com.asv.hotel.services.UserInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReportServiceImpl {
    private final ReportRepository reportRepository;
    private final ReportAttachmentInternalService reportAAttachmentService;
    private final RoomInternalService roomService;
    private final UserInternalService userService;

    @Transactional
    public ReportDTO createReport(ReportType reportType,
                                  String roomNumber,
                                  List<MultipartFile> multipartFileList) {

        Room room = roomService.findRoomByNumber(roomNumber.trim());

        if (room == null) {
            return null;
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByNickName(userDetails.getUsername());
//        User user = userService.findUserByNickName("admin_ivan");
        if (user == null) {
            return null;
        }

        Report report = Report.builder().reportStatus(ReportStatus.ISSUED)
                .reportType(reportType)
                .room(room)
                .staff(user)
                .build();

        if (multipartFileList == null || multipartFileList.isEmpty()) {
            reportRepository.save(report);
            return ReportMapper.INSTANCE.reportToReportDTO(report);
        }
        Set<ReportAttachment> reportAttachmentSet = reportAAttachmentService.generateReportAttachmentSetFromMultipartFileList(multipartFileList);
        report = addAttachmentToReport(report, reportAttachmentSet);
        report = reportRepository.save(report);
        return ReportMapper.INSTANCE.reportToReportDTO(report);
    }


    private Report addAttachmentToReport(Report report, Set<ReportAttachment> reportAttachmentSet) {
        for (ReportAttachment reportAttachment : reportAttachmentSet) {
            report.addAttachment(reportAttachment);
        }
        return report;
    }
}

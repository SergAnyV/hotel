package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.mapper.ReportMapper;
import com.asv.hotel.dto.reportdto.ReportDTO;
import com.asv.hotel.entities.Report;
import com.asv.hotel.entities.ReportAttachment;
import com.asv.hotel.entities.Room;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.enums.ReportStatus;
import com.asv.hotel.entities.enums.ReportType;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import com.asv.hotel.repositories.ReportRepository;
import com.asv.hotel.services.ReportAttachmentInternalService;
import com.asv.hotel.services.ReportService;
import com.asv.hotel.services.RoomInternalService;
import com.asv.hotel.services.UserInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportAttachmentInternalService reportAAttachmentService;
    private final RoomInternalService roomService;
    private final UserInternalService userService;

    @Transactional
    @Override
    public ReportDTO createReport(ReportType reportType,
                                  String roomNumber,
                                  List<MultipartFile> multipartFileList) {

        Room room = roomService.findRoomByNumber(roomNumber.trim());

        if (room == null) {
            return null;
        }

//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userService.findUserByNickName(userDetails.getUsername());
        User user = userService.findUserByNickName("admin_ivan");
        if (user == null) {
            return null;
        }

        Report report = Report.builder().reportStatus(ReportStatus.ISSUED)
                .reportType(reportType)
                .room(room)
                .staff(user)
                .build();

        if (isCollectionNullOrEmpty(multipartFileList)) {
            reportRepository.save(report);
            return ReportMapper.INSTANCE.reportToReportDTO(report);
        }
        Set<ReportAttachment> reportAttachmentSet = reportAAttachmentService.generateReportAttachmentSetFromMultipartFileList(multipartFileList);
        report = addAttachmentToReport(report, reportAttachmentSet);
        report = reportRepository.save(report);
        return ReportMapper.INSTANCE.reportToReportDTO(report);
    }

    @Transactional
    @Override
    public void addReportAttachmentToReport(Long reportId, List<MultipartFile> multipartFileList) {
        Report report = reportRepository.findReportById(reportId).orElse(null);

        if (report == null) {
            log.warn("Warning: Нет отчета с таким id {}", reportId);
            throw new HotelDataNotFoundException(String.format("Нет отчета с таким id %d", reportId));
        }

        if (isCollectionNullOrEmpty(multipartFileList)) {
            log.warn("Warning: нет приложенных файлов для сохранения количество");
            throw new HotelIncorrectInputData("Отсутствуют файлы для сохранения в отчет");
        }

        Set<ReportAttachment> reportAttachmentSet = multipartFileList.stream().
                map(mpf -> reportAAttachmentService.generateReportAttachmentFromMultipartFile(mpf)).
                filter(reportAttachment -> reportAttachment != null).collect(Collectors.toSet());
        if (isCollectionNullOrEmpty(reportAttachmentSet)) {
            log.warn("Warning: в приложенных файлах нет нужных для сохранения форматов");
            throw new HotelIncorrectInputData("В приложенных файлах нет нужных для сохранения форматов");
        }
        for (ReportAttachment reportAttachment : reportAttachmentSet) {
            report.addAttachment(reportAttachment);
        }
        reportRepository.save(report);
    }

    @Transactional
    @Override
    public void deleteAttachmentFromReport(Long reportId, Long reportAttachmentId) {
        int result = reportRepository.deleteReportAttachmentFromReportByID(reportId,reportAttachmentId);
        if (result==0){
            log.warn("Warning: для отчета id {} не существует пиложения с id {}",reportId,reportAttachmentId);
            throw new HotelIncorrectInputData(String.format("для отчета id %d не существует пиложения с id %d",reportId,reportAttachmentId));
        }
    }


    private boolean isCollectionNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    private Report addAttachmentToReport(Report report, Set<ReportAttachment> reportAttachmentSet) {
        for (ReportAttachment reportAttachment : reportAttachmentSet) {
            report.addAttachment(reportAttachment);
        }
        return report;
    }
}

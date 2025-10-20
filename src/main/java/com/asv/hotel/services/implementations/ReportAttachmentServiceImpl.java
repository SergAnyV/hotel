package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.mapper.ReportAttachmentMapper;
import com.asv.hotel.dto.reportattachmendto.ReportAttachmentSimpleDTO;
import com.asv.hotel.entities.ReportAttachment;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.enums.UserRole;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import com.asv.hotel.exceptions.HotelReportAttachmentException;
import com.asv.hotel.repositories.ReportAttachmentRepository;
import com.asv.hotel.services.ReportAttachmentInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportAttachmentServiceImpl implements ReportAttachmentInternalService {
    private final ReportAttachmentRepository reportAttachmentRepository;

    @Transactional
    @Override
    public ReportAttachmentSimpleDTO findReportAttachmentSimpleDTOByID(Long id) {
        ReportAttachment reportAttachment = reportAttachmentRepository.findById(id).orElse(null);
        if (reportAttachment == null) {
            return null;
        }

        User userRequester = getUserFromSecurityContext();
        if (isUserRoleAdminOrManager(userRequester)) {
            return ReportAttachmentMapper.INSTANCE.reportAttachmentToReportAttachmentSimpleDTO(reportAttachment);
        }

        User userOwner=getUserOwnerFromReportAttachment(reportAttachment);
        if(userRequester.getNickName().equals(userOwner.getNickName())){
            return ReportAttachmentMapper.INSTANCE.reportAttachmentToReportAttachmentSimpleDTO(reportAttachment);
        }

        throw new HotelReportAttachmentException(String.format("Некорректный запрос по роли и владельце приложенного " +
                "файла, запросил %s владелец %s",userRequester.getNickName(),userOwner.getNickName()));
    }


    @Transactional
    @Override
    public void deleteReportAttachmentById(Long id) {
        int result = reportAttachmentRepository.deleteReportAttachmentById(id);
        if (result == 0) {
            log.warn("Warning: нет файла для удаления с данным id {}", id);
            throw new HotelDataNotFoundException(String.format("Не существует файла для удаления с данным id = %s", id));
        }
    }


    @Override
    public List<ReportAttachment> findReportAttachmentByReportID(Long id) {
        return reportAttachmentRepository.findReportAttachmentByReportID(id);
    }

    @Override
    public ReportAttachment generateReportAttachmentFromMultipartFile(MultipartFile multipartFile) {
        String fileType = null;
        try {
            fileType = findFileTypePhoto(multipartFile);
        } catch (HotelReportAttachmentException | HotelIncorrectInputData e) {
            return null;
        }
        ReportAttachment reportAttachment = ReportAttachmentMapper.INSTANCE.multipartFileToReportAttachmentWithoutType(multipartFile);
        reportAttachment.setContentType(fileType);
        return reportAttachment;
    }

    @Override
    public Set<ReportAttachment> generateReportAttachmentSetFromMultipartFileList(List<MultipartFile> multipartFileList) {
        return multipartFileList.stream().map(multipartFile ->
                        generateReportAttachmentFromMultipartFile(multipartFile)).
                filter(reportAttachment -> reportAttachment != null).
                collect(Collectors.toSet());
    }

    @Override
    public List<ReportAttachment> findReportAttachmentForZipByReportID(Long id) {
        return reportAttachmentRepository.findReportAttachmentForZipListByReportId(id);
    }

    @Transactional
    public StreamingResponseBody findStreamingResponseBodyAttacmnetsByReportID(Long id) {
       StreamingResponseBody streamingResponseBody=getStreamingResponseBodyByReportID(id);
        User userRequester = getUserFromSecurityContext();
        if (isUserRoleAdminOrManager(userRequester)) {
            return streamingResponseBody;
        }

    }

    private String findFileTypePhoto(MultipartFile multipartFile) {
        byte[] data = null;
        try {
            data = multipartFile.getBytes();
        } catch (IOException e) {
            throw new HotelReportAttachmentException(String.format("Неверное преобразование из MultipartFile в byte[] '%s' ",
                    e));
        }
        if (data == null || data.length < 4) {
            throw new HotelIncorrectInputData("Некорректный тип входных данных файл поврежден или недостаточное количество байт");
        }

        if (data[0] == (byte) 0xFF && data[1] == (byte) 0xD8 && data[2] == (byte) 0xFF) {
            return "image/jpeg";
        }

        if (data[0] == (byte) 0x89 && data[1] == (byte) 0x50 && data[2] == (byte) 0x4E && data[3] == (byte) 0x47) {
            return "image/png";
        }

        throw new HotelIncorrectInputData("Неверный тип данных для фото");
    }

    private User getUserFromSecurityContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isUserRoleAdminOrManager(User userRequester){
        return userRequester.getType().getRole().equals(UserRole.ADMIN)||
                userRequester.getType().getRole().equals(UserRole.MANAGER);
    }
    private User getUserOwnerFromReportAttachment(ReportAttachment reportAttachment){
        return reportAttachment.getReport().getStaff();
    }

    private StreamingResponseBody getStreamingResponseBodyByReportID(Long id){
        return outputStream -> {
            try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {

                List<ReportAttachment> reportAttachmentForZipDTOList =
                        reportAttachmentRepository.findReportAttachmentForZipListByReportId(id);

                if (reportAttachmentForZipDTOList == null || reportAttachmentForZipDTOList.isEmpty()) {
                    new ZipOutputStream(outputStream).close();
                    return;
                }

                for (ReportAttachment reportAttachmentForZipDTO : reportAttachmentForZipDTOList) {
                    ZipEntry entry = new ZipEntry(
                            String.format("%s %s", reportAttachmentForZipDTO.getFileName(), reportAttachmentForZipDTO.getCreatedAt()));
                    zipOut.putNextEntry(entry);
                    zipOut.write(reportAttachmentForZipDTO.getContent());
                    zipOut.closeEntry();
                }
            }

        };
    }
}


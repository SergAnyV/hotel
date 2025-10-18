package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.mapper.ReportAttachmentMapper;
import com.asv.hotel.dto.reportattachmendto.ReportAttachmentDTO;
import com.asv.hotel.entities.ReportAttachment;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import com.asv.hotel.exceptions.HotelReportAttachmentException;
import com.asv.hotel.repositories.ReportAttachmentRepository;
import com.asv.hotel.services.ReportAttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportAttachmenServicetImpl implements ReportAttachmentService {
    private final ReportAttachmentRepository reportAttachmentRepository;


    @Transactional
    @Override
    public ReportAttachmentDTO createReportAttachment(MultipartFile multipartFile) {

        String fileType = findFileTypePhoto(multipartFile);
        ReportAttachment reportAttachment = ReportAttachmentMapper.INSTANCE.multipartFileToReportAttachmentWithoutType(multipartFile);
        reportAttachment.setContentType(fileType);
        ReportAttachment saved = reportAttachmentRepository.save(reportAttachment);

        return ReportAttachmentMapper.INSTANCE.reportAttachmentToReportAttachmentDTO(saved);
    }

    @Transactional
    @Override
    public List<ReportAttachmentDTO> createReportAttachmentBatch(List<MultipartFile> multipartFileList) {

        return multipartFileList.stream().
                map(file -> {
                    try {
                        return createReportAttachment(file);
                    } catch (HotelReportAttachmentException | HotelIncorrectInputData e) {
                        log.warn("WARN: Проблемы с приложенным файлом {}", e);
                        return null;
                    }
                }).
                filter(f -> {
                    System.out.println(f);
                    return f != null;
                }).
                collect(Collectors.toList());
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
}


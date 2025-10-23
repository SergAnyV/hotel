package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.mapper.MessageAttachmentMapper;
import com.asv.hotel.entities.MessageAttachment;
import com.asv.hotel.entities.User;
import com.asv.hotel.entities.enums.UserRole;
import com.asv.hotel.exceptions.HotelDataNotFoundException;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import com.asv.hotel.exceptions.HotelReportAttachmentException;
import com.asv.hotel.repositories.MessageAttachmentRepository;
import com.asv.hotel.services.MessageAttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageAttachmentServiceImpl implements MessageAttachmentService {
    private final MessageAttachmentRepository messageAttachmentRepository;


    @Override
    public void deleteAllMessageAttachmentByChatId(Long chatId) {
        int result = messageAttachmentRepository.deleteAllAttachmentsForChat(chatId);
        if (result == 0) {
            throw new HotelDataNotFoundException(String.format("Нет вложений в чате с id %d", chatId));
        }
    }



    public Set<MessageAttachment> generateMessageAttachmentSetFromMultipartFileList(List<MultipartFile> multipartFileList) {
        return multipartFileList.stream().map(multipartFile ->
                        generateMessageAttachmentFromMultipartFile(multipartFile)).
                filter(messageAttachment -> messageAttachment != null).
                collect(Collectors.toSet());
    }

    public MessageAttachment generateMessageAttachmentFromMultipartFile(MultipartFile multipartFile) {
        String fileType = null;
        try {
            fileType = findFileTypePhotoOrPdfOrThrowException(multipartFile);
        } catch (HotelReportAttachmentException | HotelIncorrectInputData e) {
            return null;
        }
        MessageAttachment messageAttachment = MessageAttachmentMapper.INSTANCE.multipartFileToMessageAttachmentWithoutType(multipartFile);
        messageAttachment.setContentType(fileType);
        return messageAttachment;
    }

    private String findFileTypePhotoOrPdfOrThrowException(MultipartFile multipartFile) {
        byte[] data;
        try {
            data = multipartFile.getBytes();
        } catch (IOException e) {
            throw new HotelReportAttachmentException(String.format("Неверное преобразование из MultipartFile в byte[] '%s' ",
                    e));
        }
        if (data == null || data.length < 4) {
            throw new HotelIncorrectInputData("Некорректный тип входных данных файл поврежден или недостаточное количество байт");
        }
        if (isJpeg(data)) {
            return "image/jpeg";
        }
        if (isPng(data)) {
            return "image/png";
        }

        if (isPdf(data)) {
            return "application/pdf";
        }

        throw new HotelIncorrectInputData("Неверный тип данных для фото");
    }

    private boolean isJpeg(byte[] data) {
        return data.length >= 3
                && (data[0] & 0xFF) == 0xFF
                && (data[1] & 0xFF) == 0xD8
                && (data[2] & 0xFF) == 0xFF;
    }

    private boolean isPng(byte[] data) {
        return data.length >= 4
                && (data[0] & 0xFF) == 0x89
                && (data[1] & 0xFF) == 0x50
                && (data[2] & 0xFF) == 0x4E
                && (data[3] & 0xFF) == 0x47;
    }

    private boolean isPdf(byte[] data) {
        return data.length >= 4
                && (data[0] & 0xFF) == 0x25  // '%'
                && (data[1] & 0xFF) == 0x50  // 'P'
                && (data[2] & 0xFF) == 0x44  // 'D'
                && (data[3] & 0xFF) == 0x46; // 'F'
    }

    private User getUserFromSecurityContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isUserRoleAdminOrManager(User userRequester) {
        return userRequester.getType().getRole().equals(UserRole.ADMIN) ||
                userRequester.getType().getRole().equals(UserRole.MANAGER);
    }

    private boolean isUserRoleClient(User user) {
        return user.getType().getRole().equals(UserRole.CLIENT);
    }

    private User getUserSenderFromMessageAttachment(MessageAttachment messageAttachment) {
        return messageAttachment.getSender();
    }

    private byte[] getByteAtrrayFromListMessageAttachment(List<MessageAttachment> messageAttachmentList) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            for (MessageAttachment messageAttachment : messageAttachmentList) {
                ZipEntry entry = new ZipEntry(
                        String.format("%s %s", messageAttachment.getFileName(), messageAttachment.getCreatedAt()));
                zipOut.putNextEntry(entry);
                zipOut.write(messageAttachment.getContent());
                zipOut.closeEntry();

            }
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            log.warn("Некорректное формирования zip method getStreamingResponseBodyByReportID");
            return new byte[0];
        }


    }


}

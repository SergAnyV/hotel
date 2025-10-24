package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.messageattachmentdto.MessageAttachmentDTO;
import com.asv.hotel.entities.MessageAttachment;
import com.asv.hotel.exceptions.HotelIncorrectInputData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface MessageAttachmentMapper {
    MessageAttachmentMapper INSTANCE= Mappers.getMapper(MessageAttachmentMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToLocalDate")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "size", source = "size")
    MessageAttachmentDTO messageAttachmentToMessageAttachmentDTO(MessageAttachment messageAttachment);

    @Mapping(target = "content",source = "multipartFile", qualifiedByName = "multiPartFileToByteArray")
    @Mapping(target = "fileName",source = "multipartFile", qualifiedByName = "multiPartFileGetName")
    @Mapping(target = "size",source = "multipartFile", qualifiedByName = "multiPartFileGetSize")
    @Mapping(target = "contentType", ignore = true)
    MessageAttachment multipartFileToMessageAttachmentWithoutType(MultipartFile multipartFile);


    @Named("multiPartFileToByteArray")
    default byte[] multiPartFileToByteArray(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new HotelIncorrectInputData(String.format("Неверное преобразование из MultipartFile в byte[] '%s' ",
                    e));
        }
    }

    @Named("localDateTimeToLocalDate")
    default LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    @Named("multiPartFileGetName")
    default String multiPartFileGetName(MultipartFile multipartFile) {
        return multipartFile.getOriginalFilename();
    }

    @Named("multiPartFileGetSize")
    default Long multiPartFileGetSize(MultipartFile multipartFile) {
        return multipartFile.getSize();
    }

    @Named("byteArrayToByteArrayResource")
    default ByteArrayResource byteArrayToByteArrayResource(byte[] bytes){
        return new ByteArrayResource(bytes);
    }
}

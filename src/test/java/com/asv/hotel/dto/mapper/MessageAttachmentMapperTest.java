//package com.asv.hotel.dto.mapper;
//
//import com.asv.hotel.dto.messageattachmentdto.MessageAttachmentDTO;
//import com.asv.hotel.entities.MessageAttachment;
//import com.asv.hotel.exceptions.HotelReportAttachmentException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class MessageAttachmentMapperTest {
//    @Mock
//    private MultipartFile mockMultipartFile;
//
//    private final MessageAttachmentMapper mapper = MessageAttachmentMapper.INSTANCE;
//
//
//    @Test
//    void shouldMapMessageAttachmentToDTO() {
//
//        LocalDateTime now = LocalDateTime.now();
//        MessageAttachment attachment = MessageAttachment.builder()
//                .id(1L)
//                .fileName("test.pdf")
//                .contentType("application/pdf")
//                .size(1024L)
//                .content(new byte[]{1, 2, 3})
//                .createdAt(now)
//                .build();
//
//
//        MessageAttachmentDTO dto = mapper.messageAttachmentToMessageAttachmentDTO(attachment);
//
//
//        assertThat(dto).isNotNull();
//        assertThat(dto.getId()).isEqualTo(1L);
//        assertThat(dto.getFileName()).isEqualTo("test.pdf");
//        assertThat(dto.getContentType()).isEqualTo("application/pdf");
//        assertThat(dto.getSize()).isEqualTo(1024L);
//        assertThat(dto.getCreatedAt()).isEqualTo(now.toLocalDate());
//    }
//
//
//    @Test
//    void shouldMapMultipartFileToMessageAttachment() throws Exception {
//
//        String fileName = "photo.png";
//        String contentType = "image/png";
//        byte[] content = "fake image bytes".getBytes();
//        MockMultipartFile multipartFile = new MockMultipartFile("photo.png", fileName, contentType, content);
//
//
//        MessageAttachment attachment = mapper.multipartFileToMessageAttachmentWithoutType(multipartFile);
//
//
//        assertThat(attachment).isNotNull();
//        assertThat(attachment.getFileName()).isEqualTo("photo.png");
//        assertThat(attachment.getSize()).isEqualTo(content.length);
//        assertThat(attachment.getContent()).isEqualTo(content);
//        assertThat(attachment.getContentType()).isNull();
//
//    }
//
//
//    @Test
//    void shouldThrowExceptionWhenMultipartFileFailsToReadBytes() throws Exception {
//
//        doThrow(new IOException("Read error")).when(mockMultipartFile).getBytes();
//
//
//        assertThatThrownBy(() ->
//                mapper.multipartFileToMessageAttachmentWithoutType(mockMultipartFile)
//        ).isInstanceOf(HotelReportAttachmentException.class)
//                .hasMessageContaining("Неверное преобразование из MultipartFile в byte[]");
//    }
//
//
//    @Test
//    void shouldConvertByteArrayToByteArrayResource() {
//
//        byte[] data = "hello".getBytes();
//
//        var resource = mapper.byteArrayToByteArrayResource(data);
//
//
//        assertThat(resource).isNotNull();
//        assertThat(resource.getByteArray()).isEqualTo(data);
//        assertThat(resource.contentLength()).isEqualTo(data.length);
//    }
//
//
//
//    @Test
//    void shouldConvertLocalDateTimeToLocalDate() {
//
//        LocalDateTime dateTime = LocalDateTime.of(2025, 10, 24, 15, 30);
//        LocalDate expectedDate = LocalDate.of(2025, 10, 24);
//
//
//        LocalDate result = mapper.localDateTimeToLocalDate(dateTime);
//
//
//        assertThat(result).isEqualTo(expectedDate);
//    }
//
//
//
//    @Test
//    void shouldHandleNullMessageAttachment() {
//
//        MessageAttachmentDTO dto = mapper.messageAttachmentToMessageAttachmentDTO(null);
//
//
//        assertThat(dto).isNull();
//    }
//
//
//}
package com.asv.hotel.dto.messageattachmentdto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageAttachmentDTO {
    /**
     * Уникальный идентификатор вложения.
     */
    private Long id;
    /**
     * Дата и время создания вложения.
     * для упрощения отображения (без времени).
     */
    private LocalDate createdAt;
    /**
     * Имя файла, под которым он был загружен.
     * Не должно быть {@code null} или пустым.
     * Пример: {@code "photo.jpg"}.
     */
    private String fileName;
    /**
     * MIME-тип содержимого файла.
     * Не должен быть {@code null} или пустым.
     * Пример: {@code "image/jpeg"}, {@code "application/pdf"}.
     */
    private String contentType;
    /**
     * Размер файла в байтах.
     * Всегда положительное число.
     */
    private Long size;
}

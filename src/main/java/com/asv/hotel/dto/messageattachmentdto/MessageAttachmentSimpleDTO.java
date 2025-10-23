package com.asv.hotel.dto.messageattachmentdto;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageAttachmentSimpleDTO {
    /**
     * Имя файла, используемое для формирования заголовка {@code Content-Disposition}.
     */
    private String fileName;

    /**
     * MIME-тип содержимого, используемый для заголовка {@code Content-Type}.
     * Пример: {@code "image/png"}.
     */
    private String contentType;

    /**
     * Бинарное содержимое файла, обёрнутое в {@link ByteArrayResource}.
     * Гарантирует корректную передачу через Spring MVC .
     * <p>
     * Всегда должен содержать непустой массив байтов (длина > 0).
     */
    private ByteArrayResource byteArrayResource;

}

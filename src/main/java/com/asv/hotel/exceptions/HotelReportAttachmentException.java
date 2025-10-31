package com.asv.hotel.exceptions;

import com.asv.hotel.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
/**
 * Исключение, возникающее при ошибках, связанных с обработкой вложений отчётов.
 * <p>
 * Выбрасывается в ситуациях, когда:
 * <ul>
 *   <li>Произошла ошибка при загрузке, обработке или преобразовании файла вложения (например, повреждённые данные, недопустимый формат);</li>
 *   <li>Возникла внутренняя ошибка при работе с бинарным содержимым вложения (например, при создании ZIP-архива);</li>
 *   <li>Попытка доступа к вложению отчёта запрещена по причинам, отличным от отсутствия сущности (например, нарушение логики доступа, несоответствие владельца).</li>
 * </ul>
 * <p>
 * По умолчанию сопоставляется с HTTP-статусом {@link HttpStatus#INTERNAL_SERVER_ERROR} (500),
 * так как большинство таких ошибок указывают на проблемы на стороне сервера (некорректные данные файла,
 * сбой при обработке байтов, ошибка сериализации и т.д.).
 * Однако при необходимости может быть переопределено через конструктор с {@link ErrorMessage}.
 */
public class HotelReportAttachmentException extends HotelMainException {
    /**
     * Создаёт исключение с указанием текстового сообщения об ошибке.
     * <p>
     * Использует HTTP-статус {@link HttpStatus#INTERNAL_SERVER_ERROR} по умолчанию.
     *
     * @param message описание ошибки, связанной с вложением отчёта
     *                (например, "Неверное преобразование из MultipartFile в byte[]")
     */
    public HotelReportAttachmentException(String message) {
        super(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,message));
    }
    /**
     * Создаёт исключение на основе готового объекта {@link ErrorMessage}.
     * <p>
     * Позволяет явно задать HTTP-статус и сообщение, что полезно при кастомной обработке ошибок.
     *
     * @param errorMessage объект с деталями ошибки (статус, сообщение и, опционально, временная метка)
     */
    public HotelReportAttachmentException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}

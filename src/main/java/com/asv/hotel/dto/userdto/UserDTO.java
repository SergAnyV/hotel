package com.asv.hotel.dto.userdto;


import com.asv.hotel.dto.bookingdto.BookingSimpleDTO;
import com.asv.hotel.entities.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;

@Builder
@Data
@Schema(description = "Модель данных пользователя с описание его характеристик" +
        ", списко бронирований данного пользователя ,модель передачи данных через JSON")
public class UserDTO {

    @Schema(description = "никнайм пользователя", example = "Goldman")
    @NotBlank(message = "никнайм пользователя, не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    private String nickName;

    @Schema(description = "имя пользователя", example = "Николай")
    @NotBlank(message = "имя пользователя, не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    @Pattern(
            regexp = "^[А-ЯЁа-яё]+(?:-[А-ЯЁа-яё]+)*$",
            message = "Имя может содержать только русские буквы, дефисы"
    )
    private String firstName;

    @Schema(description = "Отчество пользователя", example = "Иванович")
    @NotBlank(message = "Отчество пользователя, не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    @Pattern(
            regexp = "^[А-ЯЁа-яё]+(?:-[А-ЯЁа-яё]+)*$",
            message = "Отчество может содержать только русские буквы, дефисы"
    )
    private String fathersName;

    @Schema(description = "Фамилия  пользователя", example = "Бугульма")
    @NotBlank(message = "Фамилия пользователя, не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    @Pattern(
            regexp = "^[А-ЯЁа-яё]+(?:-[А-ЯЁа-яё]+)*$",
            message = "Фамилия может содержать только русские буквы, дефисы"
    )
    private String lastName;

    @Schema(description = "Маил  пользователя", example = "Николай")
    @Size(min = 3,max = 30,message = "количество символов 3-300")
    @Pattern(
            regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Некорректный email. Пример: user@example.com"
    )
    private String email;

    @Schema(description = "Телефон  пользователя", example = "123456789")
    @NotBlank(message = "Телефон пользователя, не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    @Pattern(
            regexp = "^[0-9]",
            message = "Некорректный email. Пример: user@example.com"
    )
    private String phoneNumber;

    @Schema(description = "Пароль  пользователя", example = "123456789")
    @NotBlank(message = "Пароль пользователя, не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    private String password;

    @Schema(description = "Тип  пользователя", example = "Клиент")
    @NotBlank(message = "Тип пользователя, не должен быть пустым")
    @Size(min = 3,max = 20,message = "количество символов 3-20")
    private String role;

    @Schema(description = "Бронирования пользователя")
    @EqualsAndHashCode.Exclude
    private Set<BookingSimpleDTO> bookingSet;

    @Schema(description = "Отчеты пользователя")
    @EqualsAndHashCode.Exclude
    private Set<Report> reports;
}

package com.asv.hotel.dto.usertypedto;

import com.asv.hotel.entities.JobType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@Schema(description = "Модель данных типа пользователя с описание и статусом активности в системе данного типа" +
        ", списком возможных выполняемых работ ,модель передачи данных через JSON")
public class UserTypeDTO {
    @Schema(description = "роль пользователя в системе", example = "Клиент")
    @NotBlank(message = "роль пользователя в системе не должна быть пустым role")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9\\s]+$", message = "Роль может содержать только буквы, цифры и пробелы")
    private String role;

    @Schema(description = "описание роли в системе отеле", example = "заказ номера и еды , обычные функции")
    @NotBlank(message = "описание роли не должно быть пустым description")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    private String description;

    @Schema(description = "активна данная роль в системе или отменена", example = "true", allowableValues = {"true", "false"} )
    @NotNull(message = "Поле isActive обязательно")
    private Boolean isActive;

    @Schema(description = "какие работы выполняет пользователь в системе отеля ,для клиента null , JobType",accessMode = Schema.AccessMode.READ_ONLY)
    private Set<JobType> jobTypeList ;
}

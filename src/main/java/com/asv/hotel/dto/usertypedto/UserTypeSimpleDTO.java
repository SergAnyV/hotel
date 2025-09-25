package com.asv.hotel.dto.usertypedto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTypeSimpleDTO {
    @NotBlank(message = "роль пользователя в системе не должна быть пустым role")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9\\s]+$", message = "Роль может содержать только буквы, цифры и пробелы")
    private String role;

    @NotBlank(message = "описание роли не должно быть пустым description")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    private String description;
}

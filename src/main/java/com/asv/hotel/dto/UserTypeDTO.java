package com.asv.hotel.dto;

import com.asv.hotel.entities.JobType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Schema(description = "Модель данных типа пользователя с описание и статусом активности в системе данного типа" +
        ", списком возможных выполняемых работ ,модель передачи данных через JSON")
public class UserTypeDTO {
    @Schema(description = "роль пользователя в системе", example = "Клиент")
    @NotBlank(message = "роль пользователя в системе не должна быть пустым role")
    private String role;

    @Schema(description = "описание роли в системе отеле", example = "заказ номера и еды , обычные функции")
    @NotBlank(message = "описание роли не должно быть пустым description")
    private String description;

    @Schema(description = "активна данная роль в системе или отменена", example = "true")
    @NotNull(message = "Поле isActive обязательно")
    private Boolean isActive;

    @Schema(description = "какие работы выполняет пользователь в системе отеля ,для клиента null , JobType")
    private Set<JobType> jobTypeList = new HashSet<>();
}

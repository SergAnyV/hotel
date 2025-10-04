package com.asv.hotel.dto.usertypedto;

import com.asv.hotel.entities.JobType;
import com.asv.hotel.entities.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Schema(description = "Модель данных типа пользователя с описание и статусом активности в системе данного типа" +
        ", списком возможных выполняемых работ ,модель передачи данных через JSON")
public class UserTypeDTO {
    @Schema(description = "название должности или клиент- пользователя в системе", example = "клиент")
    @NotBlank(message = "название должности или клиент-пользователя в системе не должна быть пустым role")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9\\s]+$", message = "Роль может содержать только буквы, цифры и пробелы")
    private String name;

    @Schema(description = "описание название должности или клиент в системе отеле", example = "заказ номера и еды , обычные функции")
    @NotBlank(message = "описание название должности или клиент  не должно быть пустым description")
    @Size(min = 3,max = 100,message = "количество символов 3-100")
    private String description;

    @Schema(description = "Роль в системе управления отелем", allowableValues = {"VISITOR","MANAGER", "ADMIN", "STAFF"})
    @NotNull
    private UserRole role;

    @Schema(description = "активна данная должнность в системе или отменена", example = "true", allowableValues = {"true", "false"} )
    @NotNull(message = "Поле isActive обязательно")
     private Boolean isActive;

    @Schema(description = "какие работы выполняет пользователь в системе отеля ,для клиента null , JobType",accessMode = Schema.AccessMode.READ_ONLY)
    @JsonIgnore
    private Set<JobType> jobTypeList ;

   }

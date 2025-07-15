package com.asv.hotel.controllers;

import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.services.UserTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-types")
@RequiredArgsConstructor
@Tag(name = "User Type Managment", description = "REST API для управления типом пользователей")
public class UserTypeController {
private final UserTypeService userTypeService;

    @Operation(summary = "Получить все типы пользователей",
            description = "Возвращает список всех типов пользователей")
    @ApiResponse(responseCode = "200", description = "Успешный запрос")
    @GetMapping
    public ResponseEntity<List<UserTypeDTO>> getAllTypes(){
        return ResponseEntity.ok(userTypeService.findAll());
    }

    @Operation(summary = "Найти тип юзера по названию роли не зависимо от регистра",
            description = "Возвращает данные типа юзера по названию типа ")
    @ApiResponse(responseCode = "200", description = "тип найден")
    @ApiResponse(responseCode = "404", description = "тип не найден")
    @GetMapping("/{role}")
    public ResponseEntity<UserTypeDTO> getTypeByRole(@PathVariable String role){
        UserTypeDTO userTypeDTO=userTypeService.findUserTypeByRole(role);
      return ResponseEntity.ok(userTypeDTO);
    }

    @Operation(summary = "Создать новый тип юзера",
            description = "создает новый тип юзера")
    @ApiResponse(responseCode = "201", description = "тип юзера создан")
    @ApiResponse(responseCode = "409", description = "тип юзера не создан")
    @PostMapping
    public ResponseEntity<UserTypeDTO> createUserType(@RequestBody @Valid UserTypeDTO userTypeDTO){
        UserTypeDTO newUserTypeDTO=userTypeService.save(userTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserTypeDTO);
    }

    @Operation(summary = "Удалить данные тип юзера",
            description = "Удалить данные существующего тип юзера")
    @ApiResponse(responseCode = "204", description = "тип юзера Удален")
    @ApiResponse(responseCode = "404", description = "тип юзера не найден")
    @DeleteMapping("/{role}")
    public ResponseEntity<UserTypeDTO> deleteUserType(@PathVariable String role){
        userTypeService.delete(role);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить данные типа юзера",
            description = "обновляет данные существующего типа юзера")
    @ApiResponse(responseCode = "200", description = "типа юзера обновлен")
    @ApiResponse(responseCode = "404", description = "типа юзера не найден")
    @ApiResponse(responseCode = "409", description = "Конфликт данных")
    @PutMapping
    public ResponseEntity<UserTypeDTO> update(@RequestBody @Valid UserTypeDTO userTypeDTO){
        UserTypeDTO updatedUserTypeDto=userTypeService.updateUserType(userTypeDTO);
        return ResponseEntity.ok(updatedUserTypeDto);
    }
}

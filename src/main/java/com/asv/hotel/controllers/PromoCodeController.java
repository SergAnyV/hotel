package com.asv.hotel.controllers;

import com.asv.hotel.dto.promocodedto.PromoCodeDTO;
import com.asv.hotel.services.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promo-codes")
@RequiredArgsConstructor
@Tag(name = "Promo Code Management", description = "API для управления промокодами")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    @Operation(summary = "Создать новый промокод",
            description = "создает новый промокод")
    @ApiResponse(responseCode = "201", description = "промокод создан")
    @ApiResponse(responseCode = "409", description = "промокод не создан")
    @PostMapping
    ResponseEntity<PromoCodeDTO> createPromoCode(@RequestBody @Valid PromoCodeDTO promoCodeDTO){
        PromoCodeDTO promoCodeDTOnew=promoCodeService.createPromoCode(promoCodeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(promoCodeDTOnew);
    }

    @Operation(summary = "Удалить промокод",
            description = "удаляет данные существующего промокод")
    @ApiResponse(responseCode = "204", description = "промокод удален")
    @ApiResponse(responseCode = "404", description = "промокод не найден")
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code){
        promoCodeService.delete(code);
        return ResponseEntity.noContent().build();
    }


}

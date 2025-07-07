package com.asv.hotel.controllers;

import com.asv.hotel.dto.PromoCodeDTO;
import com.asv.hotel.services.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promo-codes")
@RequiredArgsConstructor
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    @PostMapping
    ResponseEntity<PromoCodeDTO> createPromoCode(@RequestBody PromoCodeDTO promoCodeDTO){
        PromoCodeDTO promoCodeDTOnew=promoCodeService.createPromoCode(promoCodeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(promoCodeDTOnew);
    }


}

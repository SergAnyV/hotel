package com.asv.hotel.services;

import com.asv.hotel.dto.PromoCodeDTO;
import com.asv.hotel.dto.mapper.PromoCodeMapper;
import com.asv.hotel.entities.PromoCode;
import com.asv.hotel.exceptions.mistakes.DataAlreadyExistsException;
import com.asv.hotel.exceptions.mistakes.DataNotFoundException;
import com.asv.hotel.repositories.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    @Transactional
    public PromoCodeDTO createPromoCode(PromoCodeDTO promoCodeDTO) {
        if (promoCodeRepository.findPromoCodesByCode(promoCodeDTO.getCode()).isPresent()) {
            log.warn("Warning такой промокод уже существет", promoCodeDTO.getCode());
            throw new DataAlreadyExistsException("данный промок уже существует" + promoCodeDTO.getCode());
        }
        try {
            PromoCode promoCode = PromoCodeMapper.INSTANCE.promoCodeDTOToPromoCode(promoCodeDTO);
            return PromoCodeMapper.INSTANCE.promoCodeToPromoCodeDTO(promoCodeRepository.save(promoCode));

        } catch (DataAccessException ex) {
            log.error("Error проблема с сохранением промокода {}", ex);
            throw ex;

        }
    }

    @Transactional
    public PromoCode findActivePromoCodeByName(String code){
        try {
            return   promoCodeRepository.findActivePromoCodeByCode(code).get();
        }catch (DataNotFoundException ex){
            log.error("Error: прокод не найден", ex);
            throw new DataNotFoundException("Промокода не существует "+ code);
        }

    }




}

package com.asv.hotel.services;

import com.asv.hotel.dto.promocodedto.PromoCodeDTO;
import com.asv.hotel.dto.mapper.PromoCodeMapper;
import com.asv.hotel.entities.PromoCode;
import com.asv.hotel.exceptions.DataAlreadyExistsException;
import com.asv.hotel.exceptions.DataNotFoundException;
import com.asv.hotel.repositories.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    protected PromoCode findActivePromoCodeByName(String code) {
        try {
            return promoCodeRepository.findActivePromoCodeByCode(code).get();
        } catch (DataNotFoundException ex) {
            log.error("Error: прокод не найден", ex);
            throw new DataNotFoundException("Промокода не существует " + code);
        }

    }

    @Transactional
    public void delete(String code) {
        try {
            if (promoCodeRepository.deleteByCode(code) == 0) {
                log.warn("Warning такого промокода не  существет", code);
                throw new DataNotFoundException("данный промок не существует" + code);
            }
        } catch (DataAccessException ex) {
            log.error("Error проблема судалением промокода {}", ex);
            throw ex;
        }
    }

    @Transactional
    public List<PromoCodeDTO> findAll(){
        List<PromoCode> listPromo= promoCodeRepository.findAll();
       if( listPromo.isEmpty()){
           log.warn("Warning промокодов нет");
           throw new DataNotFoundException("промокодов нет" );
       }
       return listPromo.stream().map(promo-> {
           return PromoCodeMapper.INSTANCE.promoCodeToPromoCodeDTO(promo);

       }).collect(Collectors.toList());
    }


}

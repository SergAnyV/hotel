package com.asv.hotel.services;

import com.asv.hotel.dto.mapper.ServiceHoteMapper;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.entities.ServiceHotel;
import com.asv.hotel.exceptions.mistakes.DataAlreadyExistsException;
import com.asv.hotel.exceptions.mistakes.DataNotFoundException;
import com.asv.hotel.repositories.ServiceHotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceHotelService {
    private final ServiceHotelRepository serviceHotelRepository;

    @Transactional
    public List<ServiceHotelDTO> findAll() {
        try {
            return serviceHotelRepository.findAll().stream().map(service ->
                    ServiceHoteMapper.INSTANCE.ServiceToServiceDTO(service)
            ).collect(Collectors.toList());
        } catch (DataAccessException ex) {
            log.error("Error проблема доступа к базе ", ex);
            throw ex;
        }
    }

    @Transactional
    public ServiceHotelDTO findByTitle(String title) {
        try {
            Optional<ServiceHotel> serviceOptional =
                    serviceHotelRepository.findByTitle(title);
            if (serviceOptional.isEmpty()) {
                throw new DataNotFoundException("не существует такого сервиса");
            }
            return ServiceHoteMapper.INSTANCE.ServiceToServiceDTO(serviceOptional.get());
        } catch (DataAccessException | DataNotFoundException ex) {
            log.error(" Error ", ex);
            throw ex;
        }
    }

    @Transactional
    public ServiceHotelDTO save(ServiceHotelDTO serviceHotelDTO) {
        try {
            if (serviceHotelRepository.findByTitle(serviceHotelDTO.getTitle()).isPresent()) {
                log.warn("War такой сервис уже существует поиск по названи=ю {}", serviceHotelDTO);
                throw new DataAlreadyExistsException("такой сервис уже существует в базе");
            }
            return ServiceHoteMapper.INSTANCE.ServiceToServiceDTO(
                    serviceHotelRepository.save(
                            ServiceHoteMapper.INSTANCE.ServiceDTOToService(serviceHotelDTO)));
        } catch (DataAccessException ex) {
            log.error("Error проблем сохранения сервиса", ex);
            throw ex;
        }
    }

    @Transactional
    public void deletByTtitle(String title) {
        Optional<ServiceHotel> serviceHotelOptional=serviceHotelRepository.findByTitle(title);
        if (serviceHotelOptional.isPresent()) {
            serviceHotelRepository.delete(serviceHotelOptional.get());
        }else {
            log.error("Error данного типа сервиса не найдено {} при попытки удаления сервиса", title );
            throw new DataNotFoundException("ошибка при удаление сервиса из базы");
        }
    }

    @Transactional
    public ServiceHotelDTO update(ServiceHotelDTO serviceHotelDTO) {

        Optional<ServiceHotel> serviceHotelOptional = serviceHotelRepository.findByTitle(serviceHotelDTO.getTitle());
        if(serviceHotelOptional.isEmpty()){
            log.error("Error не найден такой сервис для обновления {}" , serviceHotelDTO);
            throw new DataNotFoundException("there is no this service");
        }
        ServiceHotel serviceHotel=serviceHotelOptional.get();
        ServiceHoteMapper.INSTANCE.updateService(serviceHotelDTO,serviceHotel);
        try {
            return ServiceHoteMapper.INSTANCE.ServiceToServiceDTO(serviceHotelRepository.save(serviceHotel));
        }catch (DataAccessException ex){
            log.error("Error проблема с сохранением и обновление сервиса отеля {} , {}" ,serviceHotelDTO,ex );
            throw ex;
        }
    }

    protected ServiceHotel findByTitleReturnEntity(String title) {
        try {
            Optional<ServiceHotel> serviceOptional =
                    serviceHotelRepository.findByTitle(title);
            if (serviceOptional.isEmpty()) {
                throw new DataNotFoundException("не существует такого сервиса");
            }
            return serviceOptional.get();
        } catch (DataAccessException | DataNotFoundException ex) {
            log.error(" Error ", ex);
            throw ex;
        }
    }
}

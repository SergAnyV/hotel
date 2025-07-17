package com.asv.hotel.services.implementations;

import com.asv.hotel.dto.mapper.ServiceHoteMapper;
import com.asv.hotel.dto.servicehoteldto.ServiceHotelDTO;
import com.asv.hotel.entities.ServiceHotel;
import com.asv.hotel.exceptions.DataAlreadyExistsException;
import com.asv.hotel.exceptions.DataNotFoundException;
import com.asv.hotel.repositories.ServiceHotelRepository;
import com.asv.hotel.services.ServiceHotelInternalService;
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
public class ServiceHotelServiceImpl implements ServiceHotelInternalService {
    private final ServiceHotelRepository serviceHotelRepository;

    @Transactional
    public List<ServiceHotelDTO> findAllHotelServices() {
        try {
            return serviceHotelRepository.findAll().stream().map(service ->
                    ServiceHoteMapper.INSTANCE.serviceToServiceDTO(service)
            ).collect(Collectors.toList());
        } catch (DataAccessException ex) {
            log.error("Error проблема доступа к базе ", ex);
            throw ex;
        }
    }

    @Transactional
    public ServiceHotelDTO findServiceHotelDTOByTitle(String title) {
        try {
            Optional<ServiceHotel> serviceOptional =
                    serviceHotelRepository.findByTitle(title);
            if (serviceOptional.isEmpty()) {
                throw new DataNotFoundException("не существует такого сервиса");
            }
            return ServiceHoteMapper.INSTANCE.serviceToServiceDTO(serviceOptional.get());
        } catch (DataAccessException | DataNotFoundException ex) {
            log.error(" Error не существует такого сервиса {}",title, ex);
            throw ex;
        }
    }

    @Transactional
    public ServiceHotelDTO createServiceHotel(ServiceHotelDTO serviceHotelDTO) {
        try {
            if (serviceHotelRepository.findByTitle(serviceHotelDTO.getTitle()).isPresent()) {
                log.warn("War такой сервис уже существует поиск по названи=ю {}", serviceHotelDTO);
                throw new DataAlreadyExistsException("такой сервис уже существует в базе");
            }
            return ServiceHoteMapper.INSTANCE.serviceToServiceDTO(
                    serviceHotelRepository.save(
                            ServiceHoteMapper.INSTANCE.serviceDTOToService(serviceHotelDTO)));
        } catch (DataAccessException ex) {
            log.error("Error проблем сохранения сервиса", ex);
            throw ex;
        }
    }

    @Transactional
    public void deletServiceHotelByTtitle(String title) {
        Optional<ServiceHotel> serviceHotelOptional=serviceHotelRepository.findByTitle(title);
        if (serviceHotelOptional.isPresent()) {
            serviceHotelRepository.delete(serviceHotelOptional.get());
        }else {
            log.error("Error данного типа сервиса не найдено {} при попытки удаления сервиса", title );
            throw new DataNotFoundException("ошибка при удаление сервиса из базы");
        }
    }

    @Transactional
    public ServiceHotelDTO changeDataServiceHotel(ServiceHotelDTO serviceHotelDTO) {

        Optional<ServiceHotel> serviceHotelOptional = serviceHotelRepository.findByTitle(serviceHotelDTO.getTitle());
        if(serviceHotelOptional.isEmpty()){
            log.error("Error не найден такой сервис для обновления {}" , serviceHotelDTO);
            throw new DataNotFoundException("there is no this service");
        }
        ServiceHotel serviceHotel=serviceHotelOptional.get();
        ServiceHoteMapper.INSTANCE.updateService(serviceHotelDTO,serviceHotel);
        try {
            return ServiceHoteMapper.INSTANCE.serviceToServiceDTO(serviceHotelRepository.save(serviceHotel));
        }catch (DataAccessException ex){
            log.error("Error проблема с сохранением и обновление сервиса отеля {} , {}" ,serviceHotelDTO,ex );
            throw ex;
        }
    }

    public ServiceHotel findServiceHotelByTitle(String title) {
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

package com.asv.hotel.services;

import com.asv.hotel.dto.mapper.RoomMapper;
import com.asv.hotel.dto.RoomDTO;
import com.asv.hotel.entities.Room;
import com.asv.hotel.exceptions.mistakes.DataAlreadyExistsException;
import com.asv.hotel.exceptions.mistakes.DataNotFoundException;
import com.asv.hotel.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;


    @Transactional(readOnly = true)
    public List<RoomDTO> findAll() {
        return roomRepository.findAll().stream().map(room -> RoomMapper.INSTANCE.roomToRoomDTO(room))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomDTO findByNumber(String number) {
        return RoomMapper.INSTANCE.roomToRoomDTO(roomRepository.findRoomByNumberLikeIgnoreCase(number)
                .orElseThrow(() -> {
                    log.warn("Error:Не существует комнаты с номером {} оошибка в методе {}", number
                            ,new Object(){}.getClass().getEnclosingMethod().getName());
                    return new DataNotFoundException("Не существует комнаты с номером " + number);
                }));
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> findByType(String type) {
        return roomRepository.findRoomByTypeLikeIgnoreCase(type).stream()
                .map(roomOptional -> {
                    if (roomOptional.isEmpty()) {
                        log.warn("Error: не существует типа комнаты {}", roomOptional.get().getType());
                        throw new DataNotFoundException("Не существует комнаты с типом " + roomOptional.get().getType());
                    }
                    return RoomMapper.INSTANCE.roomToRoomDTO(roomOptional.get());
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public RoomDTO save(RoomDTO roomDTO) {
        try {
            if (roomRepository.findRoomByNumberLikeIgnoreCase(roomDTO.getNumber()).isPresent()) {
                log.warn("Error: такая комната уже существует {} ",roomDTO.getNumber());
                throw new DataAlreadyExistsException(roomDTO.getNumber());
            }
            Room room = RoomMapper.INSTANCE.roomDTOTORomm(roomDTO);
            room.setCreatedAt(LocalDateTime.now());
            room.setUpdatedAt(LocalDateTime.now());
            return RoomMapper.INSTANCE.roomToRoomDTO(roomRepository.save(room));
        } catch (DataAccessException e) {
            log.warn("Error: проблема с доступом к базе данных , метода {}",new Object(){}.getClass().getEnclosingMethod().getName() );
            throw new DataAlreadyExistsException(roomDTO.getNumber());
        }
    }


    @Transactional
    public RoomDTO update( RoomDTO newRoomDTO) {
        Room existingRoom = roomRepository.findRoomByNumberLikeIgnoreCase(newRoomDTO.getNumber())
                .orElseThrow(() -> {
                    log.warn("Error:Не существует комнаты с номером {} оошибка в методе {}", newRoomDTO.getNumber()
                            ,new Object(){}.getClass().getEnclosingMethod().getName());
                    return new DataNotFoundException("Не существует комнаты с номером " + newRoomDTO.getNumber());
                });

        existingRoom.setNumber(newRoomDTO.getNumber());
        existingRoom.setCapacity(newRoomDTO.getCapacity());
        existingRoom.setType(newRoomDTO.getType());
        existingRoom.setDescription(newRoomDTO.getDescription());
        existingRoom.setPricePerNight(newRoomDTO.getPricePerNight());
        existingRoom.setIsAvailable(newRoomDTO.getIsAvailable());

        return RoomMapper.INSTANCE.roomToRoomDTO(roomRepository.save(existingRoom));
    }


    @Transactional
    public void delete(String number) {
        if (roomRepository.deleteRoomByNumberLikeIgnoreCase(number) == 0) {
            throw new DataNotFoundException(number);
        }
    }
    @Transactional(readOnly = true)
    public Room findByNumberReturnRoom(String number) {
        return roomRepository.findRoomByNumberLikeIgnoreCase(number).get();
    }


}

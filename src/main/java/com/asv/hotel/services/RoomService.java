package com.asv.hotel.services;

import com.asv.hotel.dto.mapper.RoomMapper;
import com.asv.hotel.dto.roomdto.RoomDTO;
import com.asv.hotel.entities.Room;
import com.asv.hotel.entities.enums.RoomType;
import com.asv.hotel.exceptions.DataAlreadyExistsException;
import com.asv.hotel.exceptions.DataNotFoundException;
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
    public List<RoomDTO> findAllRoomsDTO() {
        return roomRepository.findAll().stream().map(room -> RoomMapper.INSTANCE.roomToRoomDTO(room))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomDTO findRoomDTOByNumber(String number) {
        return RoomMapper.INSTANCE.roomToRoomDTO(roomRepository.findRoomByNumberLikeIgnoreCase(number)
                .orElseThrow(() -> {
                    log.warn("Error:Не существует комнаты с номером {} оошибка в методе {}", number
                            , new Object() {
                            }.getClass().getEnclosingMethod().getName());
                    return new DataNotFoundException("Не существует комнаты с номером " + number);
                }));
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> findRoomsDTOByType(RoomType type) {
        return roomRepository.findRoomByTypeLikeIgnoreCase(type).stream()
                .map(roomOptional -> {
                    return RoomMapper.INSTANCE.roomToRoomDTO(roomOptional);
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public RoomDTO createRoom(RoomDTO roomDTO) {
        try {
            if (roomRepository.findRoomByNumberLikeIgnoreCase(roomDTO.getNumber()).isPresent()) {
                log.warn("Error: такая комната уже существует {} ", roomDTO.getNumber());
                throw new DataAlreadyExistsException(roomDTO.getNumber());
            }
            Room room = RoomMapper.INSTANCE.roomDTOTORomm(roomDTO);
            room.setCreatedAt(LocalDateTime.now());
            room.setUpdatedAt(LocalDateTime.now());
            return RoomMapper.INSTANCE.roomToRoomDTO(roomRepository.save(room));
        } catch (DataAccessException e) {
            log.warn("Error: проблема с доступом к базе данных , метода {}", new Object() {
            }.getClass().getEnclosingMethod().getName());
            throw new DataAlreadyExistsException(roomDTO.getNumber());
        }
    }


    @Transactional
    public RoomDTO changeDataRoom(RoomDTO newRoomDTO) {
        var existingRoom = roomRepository.findRoomByNumberLikeIgnoreCase(newRoomDTO.getNumber())
                .orElseThrow(() -> {
                    log.warn("Error:Не существует комнаты с номером {} метод update в RoomService", newRoomDTO.getNumber());
                    return new DataNotFoundException("Не существует комнаты с номером " + newRoomDTO.getNumber());
                });

        RoomMapper.INSTANCE.updateRoomFromDTO(newRoomDTO, existingRoom);
        try {

            return RoomMapper.INSTANCE.roomToRoomDTO(roomRepository.save(existingRoom));
        } catch (DataAccessException ex) {
            log.error("Error проблема с обновлением комнаты {}", existingRoom, ex);
            throw new DataAccessException("Проблема с обновлением данных в комнате ") {
            };
        }

    }


    @Transactional
    public void deleteRoomByNumber(String number) {
        if (roomRepository.deleteRoomByNumberLikeIgnoreCase(number) == 0) {
            throw new DataNotFoundException(number);
        }
    }


    protected Room findRoomByNumber(String number) {
        return roomRepository.findRoomByNumberLikeIgnoreCase(number).get();
    }


}

package com.asv.hotel.services;

import com.asv.hotel.dto.mapper.RoomMapper;
import com.asv.hotel.dto.RoomDTO;
import com.asv.hotel.entities.Room;
import com.asv.hotel.exceptions.rooms.DataAlreadyExistsException;
import com.asv.hotel.exceptions.rooms.DataNotFoundException;
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
                .orElseThrow(() -> new DataNotFoundException("Не существует комнаты с номером "+ number)));
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> findByType(String type) {
        return roomRepository.findRoomByTypeLikeIgnoreCase(type).stream()
                .map(roomOptional -> {
                    if (roomOptional.isEmpty()) {
                        log.warn("пустой тип комнаты " + type);
                        return null;
                    }
                    return RoomMapper.INSTANCE.roomToRoomDTO(roomOptional.get());
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public RoomDTO save(RoomDTO roomDTO) {
        try {
            if (roomRepository.findRoomByNumberLikeIgnoreCase(roomDTO.getNumber()).isPresent()) {
                throw new DataAlreadyExistsException(roomDTO.getNumber());
            }
            Room room = RoomMapper.INSTANCE.roomDTOTORomm(roomDTO);
            room.setCreatedAt(LocalDateTime.now());
            room.setUpdatedAt(LocalDateTime.now());
            return RoomMapper.INSTANCE.roomToRoomDTO(roomRepository.save(room));
        } catch (DataAccessException e) {
            throw new DataAlreadyExistsException(roomDTO.getNumber());
        }
    }


    @Transactional
    public RoomDTO update(String number, RoomDTO newRoomDTO) {
        Room existingRoom = roomRepository.findRoomByNumberLikeIgnoreCase(number)
                .orElseThrow(() -> new DataNotFoundException(number));

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

}

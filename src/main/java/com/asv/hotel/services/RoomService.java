package com.asv.hotel.services;

import com.asv.hotel.util.DTOConvertable;
import com.asv.hotel.dto.RoomDTO;
import com.asv.hotel.entities.Room;
import com.asv.hotel.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final DTOConvertable<RoomDTO, Room> roomConvertor;

    public RoomService(RoomRepository roomRepository, DTOConvertable<RoomDTO, Room> roomConvertor) {
        this.roomRepository = roomRepository;
        this.roomConvertor = roomConvertor;
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> findAll(){
        return roomRepository.findAll().stream().map(room -> roomConvertor.convertToDTO(room))
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomDTO findByNumber(String number){
        return roomConvertor.convertToDTO(roomRepository.findRoomByNumberLikeIgnoreCase(number)
                .orElseThrow(()->new RuntimeException("There is no room with this number")));
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> findByType(String type){
        return roomRepository.findRoomByTypeLikeIgnoreCase(type).stream()
                .map(roomOptional -> roomConvertor.convertToDTO(roomOptional.get()))
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomDTO save(RoomDTO roomDTO){
        Room room= roomConvertor.convertToEntity(roomDTO);
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());
        Room savedRoom=roomRepository.save(room);
        return roomConvertor.convertToDTO(savedRoom);
    }

    @Transactional
    public RoomDTO update(String number,RoomDTO newRoomDTO){
        Room existingRoom=roomRepository.findRoomByNumberLikeIgnoreCase(number)
                .orElseThrow(()->new RuntimeException("There is no room with this number"));
        existingRoom.setCapacity(newRoomDTO.getCapacity());
        existingRoom.setType(newRoomDTO.getType());
        existingRoom.setDescription(newRoomDTO.getDescription());
        existingRoom.setPricePerNight(newRoomDTO.getPricePerNight());
        existingRoom.setIsAvailable(newRoomDTO.getIsAvailable());
        existingRoom.setUpdatedAt(LocalDateTime.now());
        Room updatedroom=roomRepository.save(existingRoom);
        return roomConvertor.convertToDTO(updatedroom);
    }

    @Transactional
    public void delete(String number){
        roomRepository.deleteRoomByNumberLikeIgnoreCase(number);
    }

}

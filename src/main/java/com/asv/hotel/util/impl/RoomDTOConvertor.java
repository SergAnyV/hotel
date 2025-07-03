package com.asv.hotel.util.impl;

import com.asv.hotel.dto.RoomDTO;
import com.asv.hotel.entities.Room;
import com.asv.hotel.util.DTOConvertable;
import org.springframework.stereotype.Service;

@Service
public final class RoomDTOConvertor implements DTOConvertable<RoomDTO, Room> {
    @Override
    public RoomDTO convertToDTO(Room room) {
        return RoomDTO.builder().capacity(room.getCapacity())
                .number(room.getNumber())
                .type(room.getType())
                .description(room.getDescription())
                .pricePerNight(room.getPricePerNight())
                .isAvailable(room.getIsAvailable())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }

    @Override
    public Room convertToEntity(RoomDTO roomDTO) {
        return Room.builder()
                .capacity(roomDTO.getCapacity())
                .number(roomDTO.getNumber())
                .type(roomDTO.getType())
                .description(roomDTO.getDescription())
                .pricePerNight(roomDTO.getPricePerNight())
                .isAvailable(roomDTO.getIsAvailable())
                .createdAt(roomDTO.getCreatedAt())
                .updatedAt(roomDTO.getUpdatedAt())
                .build();
    }
}

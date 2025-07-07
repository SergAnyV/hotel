package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.RoomDTO;
import com.asv.hotel.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE= Mappers.getMapper(RoomMapper.class);

    RoomDTO roomToRoomDTO(Room room);

    Room roomDTOTORomm(RoomDTO roomDTO);

}

package com.asv.hotel.dto.mapper;


import com.asv.hotel.dto.roomdto.RoomDTO;
import com.asv.hotel.dto.roomdto.RoomSimpleDTO;
import com.asv.hotel.entities.Room;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE= Mappers.getMapper(RoomMapper.class);


    RoomDTO roomToRoomDTO(Room room);


    Room roomDTOTORomm(RoomDTO roomDTO);


    RoomSimpleDTO roomToRoomSimpleDTO(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateRoomFromDTO(RoomDTO roomDTO, @MappingTarget Room room);


}

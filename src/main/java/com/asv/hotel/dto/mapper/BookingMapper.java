package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.bookingdto.BookingDTO;
import com.asv.hotel.dto.bookingdto.BookingSimplDTO;
import com.asv.hotel.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PromoCodeMapper.class, UserMapper.class, RoomMapper.class})
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "promoCodeDTO",source = "promoCode.code")
    @Mapping(target = "roomSimpleDTO", source = "room")
    @Mapping(target = "userSimpleDTO", source = "user")
    @Mapping(target = "serviceHotelDTOS", source = "serviceSet")
    BookingDTO bookingToBookingDTO(Booking booking);

    Booking bookingDTOToBooking(BookingSimplDTO bookingSimplDTO);

    @Mapping(target = "roomNumber", source = "room.number")
    @Mapping(target = "userSimpleDTO", source = "user")
    @Mapping(target = "promoCodeDTO",source = "promoCode.code")
    BookingSimplDTO bookingToBookingSimpleDTO(Booking booking);


    BookingDTO bookingSimpleToBookingDTO( BookingSimplDTO bookingSimplDTO);

    @Mapping(target = "serviceSet",source = "serviceSet")
    Booking bookingSimpleDTOToBooking(BookingSimplDTO bookingSimplDTO);
}

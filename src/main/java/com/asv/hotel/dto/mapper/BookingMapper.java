package com.asv.hotel.dto.mapper;

import com.asv.hotel.dto.BookingDTO;
import com.asv.hotel.dto.BookingSimpleDTO;
import com.asv.hotel.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PromoCodeMapper.class, UserMapper.class, RoomMapper.class})
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "checkInDate", source = "checkInDate",dateFormat = "yyyy-MM-dd")
    @Mapping(target = "checkOutDate", source = "checkOutDate",dateFormat = "yyyy-MM-dd")
    @Mapping(target = "persons", source = "persons")
    @Mapping(target = "roomNumber", source = "room.number")
    @Mapping(target = "userSimpleDTO", source = "user")
    @Mapping(target = "promoCodeDTO", source = "promoCode")
    @Mapping(target = "serviceSet", source = "serviceSet")
    BookingDTO bookingToBookingDTO(Booking booking);

    @Mapping(target = "room", ignore = true) // Обрабатывается отдельно через сервис
    @Mapping(target = "user", ignore = true) // Обрабатывается отдельно через сервис
    @Mapping(target = "promoCode", source = "promoCodeDTO")
    @Mapping(target = "serviceSet", source = "serviceSet")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "totalPrice", ignore = true) // Рассчитывается отдельно
    @Mapping(target = "statusOfBooking", ignore = true) // Устанавливается по умолчанию
    Booking bookingDTOToBoking(BookingDTO bookingDTO);

    @Mapping(target = "createdAt", source = "createdAt",dateFormat = "yyyy-MM-dd")
    @Mapping(target = "roomSimpleDTO",source = "room")
    @Mapping(target = "userSimpleDTO",source = "user")
    @Mapping(target = "promoCodeDTO", source = "promoCode")
    BookingSimpleDTO bookingToBookingSimpleDTO(Booking booking);
}

package com.asv.hotel.repositories;

import com.asv.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();

    Optional<Room> findRoomByNumberLikeIgnoreCase(String number);

    List<Optional<Room>> findRoomByTypeLikeIgnoreCase(String type);

    List<Optional<Room>> findRoomByPricePerNightBetween(BigDecimal min,BigDecimal max);

    void deleteRoomByNumberLikeIgnoreCase(String number);

}

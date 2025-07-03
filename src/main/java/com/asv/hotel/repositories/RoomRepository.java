package com.asv.hotel.repositories;

import com.asv.hotel.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();
    @Query(value = "SELECT * FROM rooms WHERE number ILIKE :number ", nativeQuery = true)
    Optional<Room> findRoomByNumberLikeIgnoreCase(@Param("number")String number);

    @Query(value = "SELECT * FROM rooms WHERE type ILIKE :type ", nativeQuery = true)
    List<Optional<Room>> findRoomByTypeLikeIgnoreCase(@Param("type") String type);

    @Query(value = "SELECT * FROM rooms WHERE pricePerNight BETWEEN :min AND :max", nativeQuery = true)
    List<Optional<Room>> findRoomByPricePerNightBetween(@Param("min") BigDecimal min,@Param("max") BigDecimal max);

    @Modifying
    @Query(value = "DELETE FROM rooms WHERE number ILIKE :number", nativeQuery = true)
    int deleteRoomByNumberLikeIgnoreCase(@Param("number") String number);

}

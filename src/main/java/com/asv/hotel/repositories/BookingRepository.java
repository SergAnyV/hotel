package com.asv.hotel.repositories;

import com.asv.hotel.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {


    @Query(value = "SELECT CASE WHEN COUNT(b.id) = 0 THEN true ELSE false END " +
            "FROM bookings b " +
            "WHERE b.room_id = :roomId " +
            "AND b.status != 'CANCELLED' " +
            "AND (b.check_in_date < :checkOutDate AND b.check_out_date > :checkInDate)",
            nativeQuery = true)
    boolean isRoomAvailableForDates(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);

    @Modifying
    @Query(value = "DELETE FROM bookings b WHERE b.id = :id",nativeQuery = true)
    int deleteBookingById(@Param("id") Long id);

    @Query(value = """
            SELECT b.* 
            FROM bookings b
            LEFT JOIN rooms r ON b.room_id = r.id
            WHERE r.number = :roomNumber
            ORDER BY b.check_in_date DESC
            """, nativeQuery = true)
    List<Booking> findAllByRoomNumber(@Param("roomNumber") String roomNumber);

}

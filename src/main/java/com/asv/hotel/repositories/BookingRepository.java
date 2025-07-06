package com.asv.hotel.repositories;

import com.asv.hotel.entities.Booking;
import com.asv.hotel.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Override
    List<Booking> findAll();

    @Query(value = "SELECT * FROM bookings WHERE id =: id",nativeQuery = true)
    List<Booking> findBookingById(@Param("id") Long id);

    @Query(value = "SELECT * FROM bookings WHERE user =: user",nativeQuery = true)
    List<Booking>  findBookingByUser(User user);
}

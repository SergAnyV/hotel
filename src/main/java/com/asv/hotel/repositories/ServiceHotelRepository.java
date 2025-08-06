package com.asv.hotel.repositories;

import com.asv.hotel.entities.ServiceHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceHotelRepository extends JpaRepository<ServiceHotel,Long> {
    @Query(value = "SELECT * FROM services WHERE title ILIKE :title LIMIT 1",nativeQuery = true)
    Optional<ServiceHotel>findByTitle(@Param("title") String title);

}

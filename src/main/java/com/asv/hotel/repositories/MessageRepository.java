package com.asv.hotel.repositories;

import com.asv.hotel.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m " +
            "LEFT JOIN FETCH m.sender " +
            "LEFT JOIN FETCH m.recipient " +
            "WHERE m.booking.id = :bookingId " +
            "ORDER BY m.createdAt ASC")
    List<Message> findByBookingIdOrderByCreatedAtAsc(@Param("bookingId") Long bookingId);

    long countByRecipientIdAndIsReadFalse(Long recipientId);
}

package com.asv.hotel.repositories;

import com.asv.hotel.entities.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, Long> {
    @Query(value = "SELECT * FROM message_attachments WHERE chat_id=:chatId", nativeQuery = true)
    List<MessageAttachment> findMessageAttachmentsByChat(@Param("chatId") Long chatId);

    @Query(value = "DELETE FROM message_attachments WHERE chat_id=:chatId", nativeQuery = true)
    int deleteAllAttachmentsForChat(@Param("chatId") Long chatId);

    @Query(value = "SELECT * FROM message_attachments WHERE id=:id", nativeQuery = true)
    Optional<MessageAttachment> findMessageAttachmentsById(@Param("id") Long id);
}

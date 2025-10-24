package com.asv.hotel.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "message_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name",nullable = false,length = 30)
    private String fileName;

    @Column(name = "content_type",nullable = false,length = 30)
    private String contentType;

    @Column(name = "size",nullable = false,length = 20)
    private Long size;

    @Column(name = "content",nullable = false)
    private byte[] content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @OneToOne
    @JoinColumn(name = "message_id", nullable = true)
    private Message message;


}

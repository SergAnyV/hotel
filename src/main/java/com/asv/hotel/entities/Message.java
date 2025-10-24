package com.asv.hotel.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message",nullable = false,length = 500)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id",nullable = false)
    User sender;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MessageAttachment attachment;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_id", nullable = false)
//    private Chat chat;


}

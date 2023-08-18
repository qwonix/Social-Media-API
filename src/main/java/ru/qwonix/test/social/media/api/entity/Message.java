package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserProfile sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserProfile recipient;

    @Column
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
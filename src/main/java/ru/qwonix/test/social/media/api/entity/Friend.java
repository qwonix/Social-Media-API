package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "friend")
public class Friend {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserProfile user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private UserProfile friend;

    @Enumerated(EnumType.STRING)
    private FriendshipType status;

}

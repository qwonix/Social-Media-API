package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "image")
@Entity
public class Image {

    @Id
    @Column(unique = true)
    private String imageName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

}
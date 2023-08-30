package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(imageName, image.imageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageName);
    }

}
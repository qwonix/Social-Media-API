package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "image")
@Entity
public class Image {

    @Id
    @Column(unique = true)
    private String imageName;

    private String mimeType;
}
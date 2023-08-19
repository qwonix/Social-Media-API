package ru.qwonix.test.social.media.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.qwonix.test.social.media.api.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

    Boolean existsByImageName(String name);
}

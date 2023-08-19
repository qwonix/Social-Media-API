package ru.qwonix.test.social.media.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.qwonix.test.social.media.api.entity.Post;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Boolean existsByIdAndUserUsername(UUID id, String user);
}

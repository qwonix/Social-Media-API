package ru.qwonix.test.social.media.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.qwonix.test.social.media.api.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}

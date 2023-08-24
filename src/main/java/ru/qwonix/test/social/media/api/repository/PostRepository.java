package ru.qwonix.test.social.media.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.qwonix.test.social.media.api.entity.Post;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    boolean existsByIdAndUserUsername(UUID id, String user);

    List<Post> findAllByUserIn(Collection<UserProfile> user, Pageable pageable);
}

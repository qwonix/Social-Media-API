package ru.qwonix.test.social.media.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}

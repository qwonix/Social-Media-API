package ru.qwonix.test.social.media.api.serivce;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.Optional;


public interface UserProfileService extends UserDetailsService {

    /**
     * Registers a new user profile
     *
     * @param user user profile to be registered
     * @return registered user profile
     */
    UserProfile register(UserProfile user);

    /**
     * Checks if a user profile with the specified username exists
     *
     * @param username username to check
     * @return {@code true} if a user profile with the given username exists, {@code false} otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user profile with the specified email exists
     *
     * @param email email to check
     * @return {@code true} if a user profile with the given email exists, {@code false} otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a user profile by the specified username
     *
     * @param username username to search for
     * @return An Optional containing the found user profile, or an empty Optional if not found
     */
    Optional<UserProfile> findUserByUsername(String username);
}

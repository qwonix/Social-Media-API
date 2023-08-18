package ru.qwonix.test.social.media.api.serivce;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.qwonix.test.social.media.api.entity.UserProfile;


public interface UserProfileService extends UserDetailsService {

    UserProfile register(UserProfile user);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String username);
}

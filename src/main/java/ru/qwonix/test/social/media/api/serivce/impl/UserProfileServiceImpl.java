package ru.qwonix.test.social.media.api.serivce.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.qwonix.test.social.media.api.entity.Role;
import ru.qwonix.test.social.media.api.entity.UserProfile;
import ru.qwonix.test.social.media.api.repository.UserProfileRepository;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserProfile register(UserProfile user) {
        var encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPasswordHash(encodedPassword);
        user.setRole(Role.USER);

        return userProfileRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userProfileRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userProfileRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserProfile> findUserByUsername(String username) {
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userProfileRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username " + username + "not found"));
    }
}
